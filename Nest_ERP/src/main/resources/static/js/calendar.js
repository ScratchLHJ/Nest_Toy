document.addEventListener("DOMContentLoaded", function () {
    const openBtn = document.getElementById("calendarOpenBtn");
    const modal = document.getElementById("calendarModal");
    const memoModal = document.getElementById("memoModal");
    const memoCloseBtn = document.getElementById("memoCloseBtn");
    const memoSaveBtn = document.getElementById("memoSaveBtn");
    const memoText = document.getElementById("memoText");
    const memoDateDisplay = document.getElementById("memoDateDisplay");
    const memoListArea = document.getElementById("memoListArea");
    const memoSearchList = document.getElementById("memoSearchList");
    const toggleMemoExpandBtn = document.getElementById("toggleMemoExpand");
    let calendar = null;

    if (openBtn && modal) {
        openBtn.addEventListener("click", function (e) {
            e.preventDefault();
            modal.style.display = "block";

            setTimeout(() => {
                if (!calendar) {
                    const calendarEl = document.getElementById("calendar");
                    if (calendarEl) {
                        calendar = new FullCalendar.Calendar(calendarEl, {
                            initialView: "dayGridMonth",
                            locale: "ko",
                            headerToolbar: {
                                left: "prev,next today",
                                center: "title",
                                right: "dayGridMonth,timeGridWeek,timeGridDay",
                            },
                            selectable: true,
                            dateClick: function (info) {
                                if (memoModal) {
                                    memoModal.style.display = "block";
                                    memoModal.dataset.date = info.dateStr;
                                    memoModal.dataset.id = "";
                                    memoText.value = "";
                                    memoDateDisplay.textContent = info.dateStr;
                                    loadMemosByDate(info.dateStr);
                                }
                            },
                            events: function (info, successCallback, failureCallback) {
                                fetch(`/api/calendar-memo/event-markers?start=${info.startStr}&end=${info.endStr}`)
                                    .then(res => res.json())
                                    .then(data => {
                                        const events = data.map(dateStr => ({
                                            title: '',                     // 제목은 비워두고,
                                            start: dateStr,                // 시작일
                                            display: 'background',         // 배경색 표시 방식
                                            className: 'has-memo'          // CSS 클래스 추가
                                        }));
                                        successCallback(events);
                                    })
                                    .catch(err => failureCallback(err));
                            }
                        });

                        calendar.render();
                    }
                }
            }, 50);
        });
    }

    if (memoSaveBtn) {
        memoSaveBtn.addEventListener("click", async function () {
            const memoDate = memoModal.dataset.date;
            const memoId = memoModal.dataset.id;
            const content = memoText.value.trim();
            const writer = "관리자";

            if (!content) return alert("메모 내용을 입력하세요.");

            const body = JSON.stringify({
                memoDate,
                memoContent: content,
                writerName: writer,
            });

            const method = memoId ? "PUT" : "POST";
            const url = memoId
                ? `/api/calendar-memo/${memoId}`
                : `/api/calendar-memo`;

            await fetch(url, {
                method,
                headers: { "Content-Type": "application/json" },
                body,
            });

            memoText.value = "";
            memoModal.dataset.id = "";
            loadMemosByDate(memoDate);

            document.getElementById('editingBadge').textContent = '';

        });
    }

    if (memoCloseBtn) {
        memoCloseBtn.addEventListener("click", () => {
            memoModal.style.display = "none";
        });
    }

    if (toggleMemoExpandBtn) {
        toggleMemoExpandBtn.addEventListener("click", () => {
            document
                .querySelector(".memo-modal-content")
                .classList.toggle("expanded");
        });
    }

    async function deleteMemo(id, date) {
        await fetch(`/api/calendar-memo/${id}`, { method: "DELETE" });
        if (date) loadMemosByDate(date);
    }

    window.deleteMemo = deleteMemo;

    async function loadMemosByDate(date) {
        const res = await fetch(`/api/calendar-memo/date?date=${date}`);
        const data = await res.json();
        renderMemoList(data, memoListArea);
    }

    function renderMemoList(list, container) {
        if (!container) return;
        container.innerHTML = "";
        list.forEach((memo) => {
            const div = document.createElement("div");
            div.className = "memo-item";
            div.innerHTML = `
        <div><strong>${memo.memoDate}</strong> <em>${memo.writerName}</em></div>
        <div>${memo.memoContent}</div>
        <button onclick="deleteMemo(${memo.memoId}, '${memo.memoDate}')">삭제</button>
        <button onclick="editMemo(${memo.memoId}, '${memo.memoDate}', \`${memo.memoContent.replace(/`/g, "\\`")}\`)">수정</button>
      `;
            container.appendChild(div);
        });
    }

    window.editMemo = function (id, date, content) {
        memoModal.dataset.id = id;
        memoModal.dataset.date = date;
        memoDateDisplay.textContent = date;
        memoText.value = content;
        memoModal.style.display = "block";

        document.getElementById('editingBadge').textContent = '(수정 중)';
    };

    function renderMemoTable(list) {
        const container = document.getElementById("memoSearchList");
        container.innerHTML = "";
        const table = document.createElement("table");
        table.innerHTML = `
      <thead>
        <tr><th>날짜</th><th>작성자</th><th>내용</th></tr>
      </thead>
      <tbody>
        ${list
            .map(
                (memo) => `
          <tr style="cursor:pointer" onclick="moveToDate('${memo.memoDate}')">
            <td>${memo.memoDate}</td>
            <td>${memo.writerName}</td>
            <td>${memo.memoContent}</td>
          </tr>
        `
            )
            .join("")}
      </tbody>
    `;
        container.appendChild(table);
    }

    window.handleSearchMemo = async function () {
        const fromDate = document.getElementById("searchFromDate").value;
        const toDate = document.getElementById("searchToDate").value;
        const writerName = document.getElementById("searchWriterName").value;
        const keyword = document.getElementById("searchKeyword").value;
        const params = new URLSearchParams({ fromDate, toDate, writerName, keyword });
        const res = await fetch(`/api/calendar-memo/range?${params}`);
        const list = await res.json();
        renderMemoTable(list);
    };

    window.moveToDate = function (dateStr) {
        if (calendar) {
            calendar.gotoDate(dateStr);
            memoModal.style.display = "block";
            memoModal.dataset.date = dateStr;
            memoModal.dataset.id = "";
            memoDateDisplay.textContent = dateStr;
            loadMemosByDate(dateStr);
        }
    };

    window.handleAutoComplete = async function (keyword) {
        const list = document.getElementById("autocompleteList");
        if (!keyword.trim()) {
            list.style.display = "none";
            return;
        }

        const res = await fetch(`/api/calendar-memo/autocomplete?keyword=${keyword}`);
        const data = await res.json();

        list.innerHTML = "";
        data.forEach((item) => {
            const li = document.createElement("li");
            li.textContent = `[${item.memoDate}] ${item.writerName}: ${item.memoContent}`;
            li.style.cursor = "pointer";
            li.addEventListener("click", () => {
                document.getElementById("searchKeyword").value = item.memoContent;
                list.style.display = "none";
            });
            list.appendChild(li);
        });

        list.style.display = data.length > 0 ? "block" : "none";
    };
});
