(function () {
  // Create the connector object
  var myConnector = tableau.makeConnector();

  // Define the schema
  myConnector.getSchema = function (schemaCallback) {
    var cols = [
      {
        id: "url",
        dataType: tableau.dataTypeEnum.string,
      },
    ];

    var tableSchema = {
      id: "earthquakeFeed",
      alias:
        "Earthquakes with magnitude greater than 4.5 in the last seven days",
      columns: cols,
    };

    schemaCallback([tableSchema]);
  };

  // Download the data
  myConnector.getData = function (table, doneCallback) {
    $.getJSON(
      "https://appfica.utn.edu.ec/swims/api/harvesting/thesis-records",
      function (resp) {
        console.log(resp);
        var tableData = [];

        // Iterate over the JSON object
        for (var i = 0, len = rest.length; i < len; i++) {
          tableData.push({
            url: resp[i].url,
          });
        }

        table.appendRows(tableData);
        doneCallback();
      }
    );
  };

  tableau.registerConnector(myConnector);

  // Create event listeners for when the user submits the form
  $(document).ready(function () {
    $("#submitButton").click(function () {
      tableau.connectionName = "USGS Earthquake Feed"; // This will be the data source name in Tableau
      tableau.submit(); // This sends the connector object to Tableau
    });
  });
})();
