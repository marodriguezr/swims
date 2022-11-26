CREATE SCHEMA "harvesting";

CREATE SCHEMA "auth";

CREATE SCHEMA "core";

CREATE TABLE "harvesting"."oai_sets" (
  "id" varchar PRIMARY KEY,
  "name" varchar UNIQUE NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "harvesting"."oai_records" (
  "id" varchar PRIMARY KEY,
  "url" varchar UNIQUE NOT NULL,
  "title" varchar NOT NULL,
  "creator" varchar NOT NULL,
  "subject" varchar NOT NULL,
  "description" text NOT NULL,
  "publisher" varchar NOT NULL,
  "contributor" varchar NOT NULL,
  "inferred_issue_date" date NOT NULL,
  "oai_set_id" varchar NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "harvesting"."study_variable_classes" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "long_name" varchar UNIQUE NOT NULL,
  "short_name" varchar UNIQUE NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."study_variables" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "long_name" varchar UNIQUE NOT NULL,
  "short_name" varchar UNIQUE NOT NULL,
  "is_quantitative_continuous" boolean NOT NULL,
  "is_quantitative_discrete" boolean NOT NULL,
  "is_qualitative" boolean NOT NULL,
  "is_likert" boolean NOT NULL,
  "is_boolean" boolean NOT NULL,
  "study_variable_class_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."thesis_assignments" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "user_id" int NOT NULL,
  "oai_record_id" varchar NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_survey_assignments" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "thesis_assignment_id" int NOT NULL,
  "limesurvey_survey_id" int NOT NULL,
  "limesurvey_survey_token" varchar NOT NULL,
  "is_dispatched" boolean NOT NULL DEFAULT false,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_questions" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "limesurvey_survey_id" int NOT NULL,
  "limesurvey_question_title" varchar(20) UNIQUE NOT NULL,
  "limesurvey_question_id" int UNIQUE NOT NULL,
  "study_variable_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_responses" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "limesurvey_question_title" varchar(20) NOT NULL,
  "limesurvey_answer_code" varchar(5) NOT NULL,
  "limesurvey_survey_assignment_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "auth"."users" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "first_name" varchar NOT NULL,
  "last_name" varchar NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "password" varchar NOT NULL,
  "is_root" boolean NOT NULL DEFAULT false,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "auth"."users_groups" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "user_id" int NOT NULL,
  "group_id" int NOT NULL
);

CREATE TABLE "auth"."groups" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar UNIQUE NOT NULL,
  "is_root" boolean NOT NULL DEFAULT false,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "auth"."groups_permissions" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "group_id" int NOT NULL,
  "permission_id" int NOT NULL
);

CREATE TABLE "auth"."permissions" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar UNIQUE NOT NULL,
  "webapp_related_path" varchar UNIQUE NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "core"."sysparams" (
  "key" varchar PRIMARY KEY,
  "value" varchar NOT NULL
);

CREATE UNIQUE INDEX ON "harvesting"."thesis_assignments" ("user_id", "oai_record_id");

CREATE UNIQUE INDEX ON "harvesting"."limesurvey_survey_assignments" ("thesis_assignment_id", "limesurvey_survey_id");

CREATE UNIQUE INDEX ON "auth"."users_groups" ("user_id", "group_id");

CREATE UNIQUE INDEX ON "auth"."groups_permissions" ("group_id", "permission_id");

ALTER TABLE "harvesting"."oai_records" ADD FOREIGN KEY ("oai_set_id") REFERENCES "harvesting"."oai_sets" ("id");

ALTER TABLE "harvesting"."study_variables" ADD FOREIGN KEY ("study_variable_class_id") REFERENCES "harvesting"."study_variable_classes" ("id");

ALTER TABLE "harvesting"."thesis_assignments" ADD FOREIGN KEY ("user_id") REFERENCES "auth"."users" ("id");

ALTER TABLE "harvesting"."thesis_assignments" ADD FOREIGN KEY ("oai_record_id") REFERENCES "harvesting"."oai_records" ("id");

ALTER TABLE "harvesting"."limesurvey_survey_assignments" ADD FOREIGN KEY ("thesis_assignment_id") REFERENCES "harvesting"."thesis_assignments" ("id");

ALTER TABLE "harvesting"."limesurvey_questions" ADD FOREIGN KEY ("study_variable_id") REFERENCES "harvesting"."study_variables" ("id");

ALTER TABLE "harvesting"."limesurvey_responses" ADD FOREIGN KEY ("limesurvey_question_title") REFERENCES "harvesting"."limesurvey_questions" ("limesurvey_question_title");

ALTER TABLE "harvesting"."limesurvey_responses" ADD FOREIGN KEY ("limesurvey_survey_assignment_id") REFERENCES "harvesting"."limesurvey_survey_assignments" ("id");

ALTER TABLE "auth"."users_groups" ADD FOREIGN KEY ("user_id") REFERENCES "auth"."users" ("id");

ALTER TABLE "auth"."users_groups" ADD FOREIGN KEY ("group_id") REFERENCES "auth"."groups" ("id");

ALTER TABLE "auth"."groups_permissions" ADD FOREIGN KEY ("group_id") REFERENCES "auth"."groups" ("id");

ALTER TABLE "auth"."groups_permissions" ADD FOREIGN KEY ("permission_id") REFERENCES "auth"."permissions" ("id");


-- # SPECIFIC CONSTRAINTS NON GENERATED BY DB-DIAGRAM
-- ## Restric the creation of other than one root user.
CREATE UNIQUE INDEX ON "auth"."users" (is_root) WHERE is_root = TRUE;
-- ## Restric the creation of other than one root group.
CREATE UNIQUE INDEX ON "auth"."groups" (is_root) WHERE is_root = TRUE;

