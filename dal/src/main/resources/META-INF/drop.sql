DECLARE @sql nvarchar(max) = '';

SELECT @sql += 'DROP TABLE ' + QUOTENAME([TABLE_SCHEMA]) + '.' + QUOTENAME([TABLE_NAME]) + ';'
FROM [INFORMATION_SCHEMA].[TABLES]
WHERE [TABLE_TYPE] = 'BASE TABLE';

EXEC SP_EXECUTESQL @sql;