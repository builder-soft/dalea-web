@echo off
echo -- 1.2.27 --
IF "%1" == "" GOTO error
	for %%i in (fn-*.sql) do mysql -D%1 -t -u root --default-character-set=utf8 < %%i

	for %%i in (sp-*.sql) do mysql -D%1 -t -u root --default-character-set=utf8 < %%i

	mysql -D%1 -t -u root --default-character-set=utf8 < update-database.sql

goto fin

:error

echo "No se indico nombre de la base de datos, ejecute: "
echo "$ run-all timectrl"

:fin

