(() => {
  const id = "compound_answers";
  const name = "Compound Answers";
  const apiUrl = "/api/analytics/compound-answers?study-variable-class-id=factoresExito";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "claridad_requisitos",
        dataType: tableau.dataTypeEnum.string,
        description: "Requisitos claros y bien definidos",
      },
      {
        id: "claridad_objetivos",
        dataType: tableau.dataTypeEnum.string,
        description: "Objetivos y metas claras",
      },
      {
        id: "soporte_directivos",
        dataType: tableau.dataTypeEnum.string,
        description: "Soporte de directivos de la empresa beneficiaria",
      },
      {
        id: "involucramiento_usrio",
        dataType: tableau.dataTypeEnum.string,
        description: "Involucramiento del usuario o del cliente",
      },
      {
        id: "comunicacion_efectiva",
        dataType: tableau.dataTypeEnum.string,
        description: "Comunicación efectiva entre interesados y desarrollador",
      },
      {
        id: "familiaridad_herramnt",
        dataType: tableau.dataTypeEnum.string,
        description: "Familiaridad con las herramientas de desarrollo",
      },
      {
        id: "claridad_alcance",
        dataType: tableau.dataTypeEnum.string,
        description: "Alcance del proyecto bien definido",
      },
      {
        id: "aseguramiento_calidad",
        dataType: tableau.dataTypeEnum.string,
        description: "Aseguramiento de la calidad",
      },
      {
        id: "provision_capacitacio",
        dataType: tableau.dataTypeEnum.string,
        description:
          "Provisión de tutorías o capacitaciones a los beneficiarios",
      },
      {
        id: "aseguramto_satsfacsn",
        dataType: tableau.dataTypeEnum.string,
        description: "Satisfacción del cliente",
      },
      {
        id: "reporte_prog_actualiz",
        dataType: tableau.dataTypeEnum.string,
        description: "Reporte de progreso actualizado",
      }
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
