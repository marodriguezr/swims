(() => {
  const id = "datos_informativos_tesis";
  const name = "Datos Informativos de Tesis";
  const apiUrl = "/api/analytics/compound-answers?study-variable-class-id=datosInformativosTesis";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "fecha_inicio",
        dataType: tableau.dataTypeEnum.date,
        description: "Fecha de Inicio del Proyecto de Tesis",
      },
      {
        id: "tematica_relacionada",
        dataType: tableau.dataTypeEnum.string,
        description: "Temática relacionada",
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
