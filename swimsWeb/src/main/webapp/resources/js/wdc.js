(() => {
  tableau.connectionName = "Thesis Records";
  const apiUrl = "/api/harvesting/thesis-records";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "id",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "title",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "author",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "contributor",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "subject",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "description",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "url",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "thesis_set_id",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "issue_date",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "creation_date",
        dataType: tableau.dataTypeEnum.string,
      },
    ];

    const tableSchema = {
      id: "thesisRecord",
      alias: "Thesis records",
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

  tableau.registerConnector(connector);
})();
