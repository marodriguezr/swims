(() => {
  const id = "factores_fracaso";
  const name = "Factores de Fracaso";
  const apiUrl = "/api/analytics/compound-answers?study-variable-class-id=factoresFracaso";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "falta_pruebas",
        dataType: tableau.dataTypeEnum.string,
        description: "Falta de pruebas de software",
      },
      {
        id: "falta_ident_pob_objet",
        dataType: tableau.dataTypeEnum.string,
        description: "Falta de identificación de la población objetivo",
      },
      {
        id: "cambio_requisitos",
        dataType: tableau.dataTypeEnum.string,
        description: "Cambio de requisitos y especificaciones",
      },
      {
        id: "presupuesto_insufic",
        dataType: tableau.dataTypeEnum.string,
        description: "Presupuesto insuficiente",
      },
      {
        id: "crecimi_inesp_alcanc",
        dataType: tableau.dataTypeEnum.string,
        description: "Crecimiento inesperado del alcance",
      },
      {
        id: "herramient_restric",
        dataType: tableau.dataTypeEnum.string,
        description: "Herramientas restrictivas",
      },
      {
        id: "cambio_gestor",
        dataType: tableau.dataTypeEnum.string,
        description: "Cambio de gestor del proyecto",
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
