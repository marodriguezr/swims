(() => {
  connector = tableau.makeConnector();

  connector.getSchema = (schemaCallback) => {
    const cols = [
      {
        id: "thesis_record_id",
        dataType: tableau.dataTypeEnum.string,
      },
      {
        id: "nombre_entidad_benef",
        dataType: tableau.dataTypeEnum.string,
      	description: "Nombre de la entidad beneficiaria"
      },
      {
        id: "numero_empleados",
        dataType: tableau.dataTypeEnum.int,
        description: "Número de empleados de la empresa beneficiaria"
      },
      {
        id: "tamano_empresa",
        dataType: tableau.dataTypeEnum.string,
        description: "Tamaño de la empresa beneficiaria"
      },
      {
        id: "propiedad_capital",
        dataType: tableau.dataTypeEnum.string,
        description: "Propiedad del capital"
      },
      {
        id: "sector_economia",
        dataType: tableau.dataTypeEnum.string,
        description: "Sector de la economía"
      },
      {
        id: "ambito_actuacion",
        dataType: tableau.dataTypeEnum.string,
        description: "Ámbito de actuación"
      },
      {
        id: "ubicacion_afeccion",
        dataType: tableau.dataTypeEnum.string,
        description: "Ubicación de Afección"
      },
      {
        id: "concepto_entrega",
        dataType: tableau.dataTypeEnum.string,
        description: "Concepto de Entrega del Producto"
      },
      {
        id: "numero_per_beneficiada",
        dataType: tableau.dataTypeEnum.int,
        description: "Número de personas directamente beneficiadas por el producto de software"
      },
      {
        id: "num_per_indir_benficiad",
        dataType: tableau.dataTypeEnum.int,
        description: "Número de personas indirectamente beneficiadas por el producto de software"
      },
       {
        id: "ods_focalizado",
        dataType: tableau.dataTypeEnum.string,
        description: "Objetivo de Desarrollo Sostenible focalizado"
      },
      {
        id: "estado_actual",
        dataType: tableau.dataTypeEnum.string,
        description: "Estado actual del producto de software"
      },
      {
        id: "presupuesto",
        dataType: tableau.dataTypeEnum.float,
        description: "Presupuesto del Proyecto de Tesis"
      },
      {
        id: "ingreso_bruto_empresa",
        dataType: tableau.dataTypeEnum.float,
        description: "Ingreso bruto anual de la empresa"
      },
      {
        id: "rlcn_medioambiente",
        dataType: tableau.dataTypeEnum.string,
        description: "Relación con áreas del medio ambiente"
      },
      {
        id: "claridad_requisitos",
        dataType: tableau.dataTypeEnum.string,
        description: "Requisitos claros y bien definidos"
      },
      {
        id: "claridad_objetivos",
        dataType: tableau.dataTypeEnum.string,
        description: "Objetivos y metas claras"
      },
      {
        id: "soporte_directivos",
        dataType: tableau.dataTypeEnum.string,
        description: "Soporte de directivos de la empresa beneficiaria"
      },
      {
        id: "involucramiento_usrio",
        dataType: tableau.dataTypeEnum.string,
        description: "Involucramiento del usuario o del cliente"
      },
      {
        id: "comunicacion_efectiva",
        dataType: tableau.dataTypeEnum.string,
        description: "Comunicación efectiva entre interesados y desarrollador"
      },
      {
        id: "familiaridad_herramnt",
        dataType: tableau.dataTypeEnum.string,
        description: "Familiaridad con las herramientas de desarrollo"
      },
      {
        id: "claridad_alcance",
        dataType: tableau.dataTypeEnum.string,
        description: "Alcance del proyecto bien definido"
      },
      {
        id: "aseguramiento_calidad",
        dataType: tableau.dataTypeEnum.string,
        description: "Aseguramiento de la calidad"
      },
      {
        id: "provision_capacitacio",
        dataType: tableau.dataTypeEnum.string,
        description: "Provisión de tutorías o capacitaciones a los beneficiarios"
      },
      {
        id: "aseguramto_satsfacsn",
        dataType: tableau.dataTypeEnum.string,
        description: "Satisfacción del cliente"
      },
      {
        id: "reporte_prog_actualiz",
        dataType: tableau.dataTypeEnum.string,
        description: "Reporte de progreso actualizado"
      },
      {
        id: "falta_pruebas",
        dataType: tableau.dataTypeEnum.string,
        description: "Falta de pruebas de software"
      },
      {
        id: "falta_ident_pob_objet",
        dataType: tableau.dataTypeEnum.string,
        description: "Falta de identificación de la población objetivo"
      },
      {
        id: "cambio_requisitos",
        dataType: tableau.dataTypeEnum.string,
        description: "Cambio de requisitos y especificaciones"
      },
      {
        id: "presupuesto_insufic",
        dataType: tableau.dataTypeEnum.string,
        description: "Presupuesto insuficiente"
      },
      {
        id: "crecimi_inesp_alcanc",
        dataType: tableau.dataTypeEnum.string,
        description: "Crecimiento inesperado del alcance"
      },
      {
        id: "herramient_restric",
        dataType: tableau.dataTypeEnum.string,
        description: "Herramientas restrictivas"
      },
      {
        id: "cambio_gestor",
        dataType: tableau.dataTypeEnum.string,
        description: "Cambio de gestor del proyecto"
      },
      {
        id: "lenguaje",
        dataType: tableau.dataTypeEnum.string,
        description: "Lenguaje utilizado"
      },
      {
        id: "framework",
        dataType: tableau.dataTypeEnum.string,
        description: "Framework"
      },
      {
        id: "libreria",
        dataType: tableau.dataTypeEnum.string,
        description: "Librería"
      },
      {
        id: "entorno_desarrollo",
        dataType: tableau.dataTypeEnum.string,
        description: "Entorno de desarrollo"
      },
      {
        id: "dbms",
        dataType: tableau.dataTypeEnum.string,
        description: "Sistema gestor de base de datos"
      },
      {
        id: "servidor_despliegue",
        dataType: tableau.dataTypeEnum.string,
        description: "Servidor de despliegue"
      },
      {
        id: "so_despliegue",
        dataType: tableau.dataTypeEnum.string,
        description: "Sistema operativo de despliegue"
      },
      {
        id: "sw_apoyo",
        dataType: tableau.dataTypeEnum.string,
        description: "Software de apoyo"
      },
      {
        id: "iaas_provider",
        dataType: tableau.dataTypeEnum.string,
        description: "Proveedor de infraestructura como servicio"
      },
      {
        id: "metodologia_desarroll",
        dataType: tableau.dataTypeEnum.string,
        description: "Metodología de desarrollo"
      },
      {
        id: "fecha_inicio",
        dataType: tableau.dataTypeEnum.date,
        description: "Fecha de Inicio del Proyecto de Tesis"
      },
      {
        id: "tematica_relacionada",
        dataType: tableau.dataTypeEnum.string,
        description: "Temática relacionada"
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