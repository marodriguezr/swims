CREATE SCHEMA "harvesting";

CREATE SCHEMA "auth";

CREATE SCHEMA "core";

CREATE TABLE "harvesting"."thesis_sets" (
  "id" varchar PRIMARY KEY,
  "name" varchar UNIQUE NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "harvesting"."thesis_records" (
  "id" varchar PRIMARY KEY,
  "url" varchar UNIQUE NOT NULL,
  "title" varchar NOT NULL,
  "creator" varchar NOT NULL,
  "subject" varchar NOT NULL,
  "description" text NOT NULL,
  "contributor" varchar NOT NULL,
  "inferred_issue_date" date NOT NULL,
  "inferred_creation_date" date NOT NULL,
  "thesis_set_id" varchar NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now()),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "harvesting"."study_variable_classes" (
  "id" varchar(32) PRIMARY KEY,
  "name" varchar(256) UNIQUE NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."study_variables" (
  "id" varchar(32) PRIMARY KEY,
  "name" varchar(256) UNIQUE NOT NULL,
  "is_numeric_continuous" boolean NOT NULL,
  "is_numeric_discrete" boolean NOT NULL,
  "is_categorical_nominal" boolean NOT NULL,
  "is_categorical_ordinal" boolean NOT NULL,
  "study_variable_class_id" varchar(32) NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."thesis_assignments" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "user_id" int NOT NULL,
  "thesis_record_id" varchar NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_survey_assignments" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "thesis_assignment_id" int NOT NULL,
  "limesurvey_survey_id" int NOT NULL,
  "limesurvey_survey_token" varchar UNIQUE NOT NULL,
  "is_dispatched" boolean NOT NULL DEFAULT false,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_questions" (
  "id" varchar PRIMARY KEY,
  "limesurvey_question_title" varchar(20) NOT NULL,
  "limesurvey_survey_id" int NOT NULL,
  "study_variable_id" varchar(32) NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."questions" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "study_variable_id" varchar(32) UNIQUE NOT NULL,
  "question" varchar(512) NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."expected_answers" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "expected_answer" varchar(256) NOT NULL,
  "question_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."answers" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "question_id" int NOT NULL,
  "expected_answer_id" int NOT NULL,
  "thesis_assignment_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_answers" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "limesurvey_question_id" varchar NOT NULL,
  "answer" varchar NOT NULL,
  "limesurvey_survey_assignment_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (now()),
  "updated_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "harvesting"."limesurvey_unexpected_answers" (
  "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "limesurvey_question_id" varchar NOT NULL,
  "answer" varchar NOT NULL,
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

CREATE UNIQUE INDEX ON "harvesting"."thesis_assignments" ("user_id", "thesis_record_id");

CREATE UNIQUE INDEX ON "harvesting"."limesurvey_survey_assignments" ("thesis_assignment_id", "limesurvey_survey_id");

CREATE UNIQUE INDEX ON "harvesting"."expected_answers" ("expected_answer", "question_id");

CREATE UNIQUE INDEX ON "harvesting"."answers" ("expected_answer_id", "thesis_assignment_id");

CREATE UNIQUE INDEX ON "auth"."users_groups" ("user_id", "group_id");

CREATE UNIQUE INDEX ON "auth"."groups_permissions" ("group_id", "permission_id");

COMMENT ON COLUMN "harvesting"."limesurvey_questions"."limesurvey_question_title" IS 'represents the field code that appears in limesurveys user interface both for questions and subquestions. Named as title in limesurveys db tables and api responses ';

ALTER TABLE "harvesting"."thesis_records" ADD FOREIGN KEY ("thesis_set_id") REFERENCES "harvesting"."thesis_sets" ("id");

ALTER TABLE "harvesting"."study_variables" ADD FOREIGN KEY ("study_variable_class_id") REFERENCES "harvesting"."study_variable_classes" ("id");

ALTER TABLE "harvesting"."thesis_assignments" ADD FOREIGN KEY ("user_id") REFERENCES "auth"."users" ("id");

ALTER TABLE "harvesting"."thesis_assignments" ADD FOREIGN KEY ("thesis_record_id") REFERENCES "harvesting"."thesis_records" ("id");

ALTER TABLE "harvesting"."limesurvey_survey_assignments" ADD FOREIGN KEY ("thesis_assignment_id") REFERENCES "harvesting"."thesis_assignments" ("id");

ALTER TABLE "harvesting"."limesurvey_questions" ADD FOREIGN KEY ("study_variable_id") REFERENCES "harvesting"."study_variables" ("id");

ALTER TABLE "harvesting"."questions" ADD FOREIGN KEY ("study_variable_id") REFERENCES "harvesting"."study_variables" ("id");

ALTER TABLE "harvesting"."expected_answers" ADD FOREIGN KEY ("question_id") REFERENCES "harvesting"."questions" ("id");

ALTER TABLE "harvesting"."answers" ADD FOREIGN KEY ("question_id") REFERENCES "harvesting"."questions" ("id");

ALTER TABLE "harvesting"."answers" ADD FOREIGN KEY ("expected_answer_id") REFERENCES "harvesting"."expected_answers" ("id");

ALTER TABLE "harvesting"."answers" ADD FOREIGN KEY ("thesis_assignment_id") REFERENCES "harvesting"."thesis_assignments" ("id");

ALTER TABLE "harvesting"."limesurvey_answers" ADD FOREIGN KEY ("limesurvey_question_id") REFERENCES "harvesting"."limesurvey_questions" ("id");

ALTER TABLE "harvesting"."limesurvey_answers" ADD FOREIGN KEY ("limesurvey_survey_assignment_id") REFERENCES "harvesting"."limesurvey_survey_assignments" ("id");

ALTER TABLE "harvesting"."limesurvey_unexpected_answers" ADD FOREIGN KEY ("limesurvey_question_id") REFERENCES "harvesting"."limesurvey_questions" ("id");

ALTER TABLE "harvesting"."limesurvey_unexpected_answers" ADD FOREIGN KEY ("limesurvey_survey_assignment_id") REFERENCES "harvesting"."limesurvey_survey_assignments" ("id");

ALTER TABLE "auth"."users_groups" ADD FOREIGN KEY ("user_id") REFERENCES "auth"."users" ("id");

ALTER TABLE "auth"."users_groups" ADD FOREIGN KEY ("group_id") REFERENCES "auth"."groups" ("id");

ALTER TABLE "auth"."groups_permissions" ADD FOREIGN KEY ("group_id") REFERENCES "auth"."groups" ("id");

ALTER TABLE "auth"."groups_permissions" ADD FOREIGN KEY ("permission_id") REFERENCES "auth"."permissions" ("id");


-- # SPECIFIC CONSTRAINTS NON GENERATED BY DB-DIAGRAM
-- ## Restric the creation of other than one root user.
CREATE UNIQUE INDEX ON "auth"."users" (is_root) WHERE is_root = TRUE;
-- ## Restric the creation of other than one root group.
CREATE UNIQUE INDEX ON "auth"."groups" (is_root) WHERE is_root = TRUE;

-- # VIEWS
create view harvesting.limesurvey_survey_ids_inner_thesis_assignments as
select
	lsa.limesurvey_survey_id,
	ta.thesis_record_id,
	concat(lsa.limesurvey_survey_id, ta.thesis_record_id) as id 
from
	harvesting.thesis_assignments ta,
	harvesting.limesurvey_survey_assignments lsa
where
	ta.id = lsa.thesis_assignment_id;
	
create view harvesting.undispatched_thesis_assignments as 
select
	distinct (ta.id),
	ta.user_id,
	ta.thesis_record_id
from
	harvesting.thesis_assignments ta,
	harvesting.limesurvey_survey_assignments lsa
where
	ta.id = lsa.thesis_assignment_id
	and lsa.is_dispatched = false;

create view harvesting.undispatched_survey_assignments as 
select
	lsa.id,
	ta.user_id,
	ta.thesis_record_id
from
	harvesting.thesis_assignments ta,
	harvesting.limesurvey_survey_assignments lsa
where
	ta.id = lsa.thesis_assignment_id
	and lsa.is_dispatched = false;
	
create view harvesting.unassigned_thesis_assignment_ids as 
select
	 or2.id
from
	harvesting.thesis_records or2
where
	not exists (
	select
	from
		harvesting.thesis_assignments ta
	where
		or2.id = ta.thesis_record_id);
	
CREATE SCHEMA "analytics";
create view analytics.compound_answers as 
select
	concat(ta.thesis_record_id,
	la.answer,
	lq.limesurvey_question_title) as id, 
	ta.thesis_record_id,
	la.answer,
	lq.limesurvey_question_title,
	lq.study_variable_id,
	sv.study_variable_class_id,
	sv.is_numeric_continuous,
	sv.is_numeric_discrete,
	sv.is_categorical_nominal,
	sv.is_categorical_ordinal
from
	harvesting.thesis_assignments ta,
	harvesting.limesurvey_survey_assignments lsa,
	harvesting.limesurvey_answers la,
	harvesting.limesurvey_questions lq,
	harvesting.study_variables sv
where
	ta.id = lsa.thesis_assignment_id
	and la.limesurvey_survey_assignment_id = lsa.id
	and la.limesurvey_question_id = lq.id
	and lq.study_variable_id = sv.id;