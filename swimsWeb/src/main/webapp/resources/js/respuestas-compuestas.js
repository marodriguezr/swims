(() => {
  connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "nombreEntidadBenef",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "numeroEmpleados",
        dataType: tableau.dataTypeEnum.int,
      },
      {
        id: "tamanoEmpresa",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "propiedadCapital",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "sectorEconomia",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "ambitoActuacion",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "ubicacionAfeccion",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "conceptoEntrega",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "numeroPerBeneficiada",
        dataType: tableau.dataTypeEnum.int,
      },
      {
        id: "numPerIndirBenficiad",
        dataType: tableau.dataTypeEnum.int,
      },
       {
        id: "odsFocalizado",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "estadoActual",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "presupuesto",
        dataType: tableau.dataTypeEnum.float,
      },
      {
        id: "ingresoBrutoEmpresa",
        dataType: tableau.dataTypeEnum.float,
      },
      {
        id: "rlcnMedioambiente",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "claridadRequisitos",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "claridadObjetivos",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "soporteDirectivos",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "involucramientoUsrio",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "comunicacionEfectiva",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "familiaridadHerramnt",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "claridadAlcance",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "aseguramientoCalidad",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "provisionCapacitacio",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "aseguramtoSatsfacsn",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "reporteProgActualiz",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "faltaPruebas",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "faltaIdentPobObjet",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "cambioRequisitos",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "presupuestoInsufic",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "crecimiInespAlcanc",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "herramientRestric",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "cambioGestor",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "lenguaje",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "framework",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "libreria",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "entornoDesarrollo",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "dbms",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "servidorDespliegue",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "soDespliegue",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "swApoyo",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "iaasProvider",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "metodologiaDesarroll",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "fechaInicio",
        dataType: tableau.dataTypeEnum.date,
      },
      {
        id: "tematicaRelacionada",
        dataType: tableau.dataTypeEnum.string,
      },
    ];

    const tableSchema = {
      id: "compoundAnswer",
      alias: "Compound Answers",
      columns: cols,
    };

    schemaCallback([tableSchema]);
  };

  connector.getData = (table, doneCallback) => {
    fetch("https://appfica.utn.edu.ec/swims/api/analytics/compound-answers")
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
  tableau.connectionName = "Compound Answers";
})();