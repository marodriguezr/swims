(() => {
  const id = "compound_answers";
  const name = "Compound Answers";
  const apiUrl = "/api/analytics/compound-answers?study-variable-class-id=indicadoresImpactoEconomico";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "presupuesto",
        dataType: tableau.dataTypeEnum.float,
        description: "Presupuesto del Proyecto de Tesis",
      },
      {
        id: "ingreso_bruto_empresa",
        dataType: tableau.dataTypeEnum.float,
        description: "Ingreso bruto anual de la empresa",
      },
    ];

    const tableSchema = {
      id: id,
      alias: name,
      columns: cols,
    };

    schemaCallback([tableSchema]);
  };

  connector.getData = (table, doneCallback) => {
    const context = window.location.pathname.substring(
      0,
      window.location.pathname.indexOf("/", 2)
    );
    const url =
      window.location.protocol + "//" + window.location.host + context + apiUrl;

    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        table.appendRows(data);
        doneCallback();
      })
      .catch(() => {
        table.appendRows([]);
        doneCallback();
      });
  };

  tableau.connectionName = name;
  tableau.registerConnector(connector);
})();
