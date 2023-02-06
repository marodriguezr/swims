(() => {
  const id = "compound_answers";
  const name = "Compound Answers";
  const apiUrl = "/api/analytics/compound-answers?study-variable-class-id=indicadoresImpactoSocial";

  const connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "thesis_record_id",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "nombre_entidad_benef",
        dataType: tableau.dataTypeEnum.string,
        description: "Nombre de la entidad beneficiaria",
      },
      {
        id: "numero_empleados",
        dataType: tableau.dataTypeEnum.int,
        description: "Número de empleados de la empresa beneficiaria",
      },
      {
        id: "tamano_empresa",
        dataType: tableau.dataTypeEnum.string,
        description: "Tamaño de la empresa beneficiaria",
      },
      {
        id: "propiedad_capital",
        dataType: tableau.dataTypeEnum.string,
        description: "Propiedad del capital",
      },
      {
        id: "sector_economia",
        dataType: tableau.dataTypeEnum.string,
        description: "Sector de la economía",
      },
      {
        id: "ambito_actuacion",
        dataType: tableau.dataTypeEnum.string,
        description: "Ámbito de actuación",
      },
      {
        id: "ubicacion_afeccion",
        dataType: tableau.dataTypeEnum.string,
        description: "Ubicación de Afección",
      },
      {
        id: "concepto_entrega",
        dataType: tableau.dataTypeEnum.string,
        description: "Concepto de Entrega del Producto",
      },
      {
        id: "numero_per_beneficiada",
        dataType: tableau.dataTypeEnum.int,
        description:
          "Número de personas directamente beneficiadas por el producto de software",
      },
      {
        id: "num_per_indir_benficiad",
        dataType: tableau.dataTypeEnum.int,
        description:
          "Número de personas indirectamente beneficiadas por el producto de software",
      },
      {
        id: "ods_focalizado",
        dataType: tableau.dataTypeEnum.string,
        description: "Objetivo de Desarrollo Sostenible focalizado",
      },
      {
        id: "estado_actual",
        dataType: tableau.dataTypeEnum.string,
        description: "Estado actual del producto de software",
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
