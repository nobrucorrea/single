UPDATE Produto SET IdCalculoICMS = '' WHERE IdCalculoICMS LIKE '0000F%';
DELETE FROM CalculoICMS_UF WHERE IdCalculoICMS LIKE '0000F%';
DELETE FROM CalculoICMS WHERE IdCalculoICMS LIKE '0000F%';
UPDATE Produto SET IdClassificacaoFiscal = '' WHERE IdClassificacaoFiscal LIKE '0000F%';
DELETE FROM ClassificacaoFiscalItem WHERE IdClassificacaoFiscalItem LIKE '0000F%';
DELETE FROM ClassificacaoFiscal WHERE IdClassificacaoFiscal LIKE '0000F%';