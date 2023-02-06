(() => {
  const id = "recursos_desarrollo";
  const name = "Recursos de Desarrollo";
  const apiUrl = "/api/analytics/compound-unexpected-answers?study-variable-class-id=recursosDesarrollo";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "thesis_record_id",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "lenguaje",
        dataType: tableau.dataTypeEnum.string,
        description: "Lenguaje utilizado",
      },
      {
        id: "framework",
        dataType: tableau.dataTypeEnum.string,
        description: "Framework",
      },
      {
        id: "libreria",
        dataType: tableau.dataTypeEnum.string,
        description: "Librería",
      },
      {
        id: "entorno_desarrollo",
        dataType: tableau.dataTypeEnum.string,
        description: "Entorno de desarrollo",
      },
      {
        id: "dbms",
        dataType: tableau.dataTypeEnum.string,
        description: "Sistema gestor de base de datos",
      },
      {
        id: "servidor_despliegue",
        dataType: tableau.dataTypeEnum.string,
        description: "Servidor de despliegue",
      },
      {
        id: "so_despliegue",
        dataType: tableau.dataTypeEnum.string,
        description: "Sistema operativo de despliegue",
      },
      {
        id: "sw_apoyo",
        dataType: tableau.dataTypeEnum.string,
        description: "Software de apoyo",
      },
      {
        id: "iaas_provider",
        dataType: tableau.dataTypeEnum.string,
        description: "Proveedor de infraestructura como servicio",
      },
      {
        id: "metodologia_desarroll",
        dataType: tableau.dataTypeEnum.string,
        description: "Metodología de desarrollo",
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
