ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;
create user nest identified by java1234;
grant connect, resource, dba to nest;