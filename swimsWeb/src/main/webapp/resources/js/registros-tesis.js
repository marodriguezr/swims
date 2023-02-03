(() => {
  connector = tableau.makeConnector();

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
        dataType: tableau.dataTypeEnum.date,
      },
      {
        id: "creation_date",
        dataType: tableau.dataTypeEnum.date,
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
    fetch("https://appfica.utn.edu.ec/swims/api/harvesting/thesis-records")
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
  tableau.connectionName = "Thesis Records";
})();