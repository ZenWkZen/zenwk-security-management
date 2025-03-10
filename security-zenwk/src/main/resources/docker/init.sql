DO $$ 
BEGIN 
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'postgres') THEN
        CREATE ROLE postgres WITH LOGIN SUPERUSER PASSWORD 'calegria';
    END IF;
END $$;


--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Homebrew)
-- Dumped by pg_dump version 16.4 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


--
-- Name: sec_token; Type: TABLE; Schema: public; Owner: postgres
--
create table public.sec_token
(
    sectokid            serial   not null primary key,
    sectokcode          varchar(10)   not null unique,
    sectokemail         varchar(50) NOT NULL UNIQUE,
    sectokcreationdate  timestamp NOT NULL  default CURRENT_TIMESTAMP,
    sectoexpirationdate timestamp NOT NULL,
    sectokcreateuser    varchar(50)
);
alter table public.sec_token  owner to postgres;

comment on table public.sec_token is 'Tabla que almacena los tokens de verficación del usuario.';
comment on column public.sec_token.sectokid is 'Identificador único.';
comment on column public.sec_token.sectokcode is 'Nombre de código de token único.';
comment on column public.sec_token.sectokemail is 'Email del usuario.';
comment on column public.sec_token.sectokcreationdate is 'Fecha y hora de creación del registro.';
comment on column public.sec_token.sectoexpirationdate is 'Fecha y hora de expiración de token.';
comment on column public.sec_token.sectokcreateuser is 'Usuario que creó el registro.';
--
-- Name: log_sec; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log_sec (
    logsecuseid integer NOT NULL,
    logsecusestatuscode bigint,
    logsecuseipaddress character varying(20),
    logsecuseuseragent character varying(50),
    logsecuseexecutiontime character varying(30),
    logsecuseservicename character varying(200),
    logsecusemethod character varying(50),
    logsecuseurl character varying(200),
    logsecuserequest text,
    logsecuseresponse text,
    logsecuseerrormessage text,
    logsecusecreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    logsecuseusercreation character varying(50)
);


ALTER TABLE public.log_sec OWNER TO postgres;

--
-- Name: TABLE log_sec; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.log_sec IS 'Tabla que almacena los logs del componente uer securiy.';


--
-- Name: COLUMN log_sec.logsecuseid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseid IS 'Identificador único del registro historico.';


--
-- Name: COLUMN log_sec.logsecusestatuscode; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecusestatuscode IS 'Código de estado http';


--
-- Name: COLUMN log_sec.logsecuseipaddress; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseipaddress IS 'Identificador único del registro historico.';


--
-- Name: COLUMN log_sec.logsecuseuseragent; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseuseragent IS 'Identificador único del registro historico.';


--
-- Name: COLUMN log_sec.logsecuseexecutiontime; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseexecutiontime IS 'Identificador único del registro historico.';


--
-- Name: COLUMN log_sec.logsecuseservicename; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseservicename IS 'Identificador único del registro historico.';


--
-- Name: COLUMN log_sec.logsecusemethod; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecusemethod IS 'Método de la solicitud';


--
-- Name: COLUMN log_sec.logsecuseurl; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseurl IS 'Ruta con  recurso rest expuesto';


--
-- Name: COLUMN log_sec.logsecuserequest; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuserequest IS 'Petición';


--
-- Name: COLUMN log_sec.logsecuseresponse; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseresponse IS 'Resultado';


--
-- Name: COLUMN log_sec.logsecuseerrormessage; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseerrormessage IS 'Mensaje de error éxito';


--
-- Name: COLUMN log_sec.logsecusecreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecusecreationdate IS 'Fecha y hora de creación del registro.';


--
-- Name: COLUMN log_sec.logsecuseusercreation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.log_sec.logsecuseusercreation IS 'Nombre de usuario de creación del registro.';


--
-- Name: log_sec_logsecuseid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.log_sec_logsecuseid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.log_sec_logsecuseid_seq OWNER TO postgres;

--
-- Name: log_sec_logsecuseid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.log_sec_logsecuseid_seq OWNED BY public.log_sec.logsecuseid;


--
-- Name: sec_permission; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_permission (
    secperidpermission integer NOT NULL,
    secpername character varying(100) NOT NULL,
    secperdescription character varying(255),
    secpercreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    secpermodificationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    secpercreationuser character varying(50),
    secpermodificationuser character varying(50),
    secperresource character varying(200),
    secpermethod character varying(20),
    secperoperation character varying(50) NOT NULL,
    CONSTRAINT chk_secoperation CHECK (((secperoperation)::text = ANY ((ARRAY['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying, 'GET'::character varying, 'LIST'::character varying])::text[])))
);


ALTER TABLE public.sec_permission OWNER TO postgres;

--
-- Name: TABLE sec_permission; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_permission IS 'Tabla que almacena los permisos del sistema.';


--
-- Name: COLUMN sec_permission.secperidpermission; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secperidpermission IS 'Identificador único del permiso.';


--
-- Name: COLUMN sec_permission.secpername; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secpername IS 'Nombre del permiso.';


--
-- Name: COLUMN sec_permission.secperdescription; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secperdescription IS 'Descripción del permiso.';


--
-- Name: COLUMN sec_permission.secpercreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secpercreationdate IS 'Fecha y hora de creación del registro.';


--
-- Name: COLUMN sec_permission.secpermodificationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secpermodificationdate IS 'Usuario que realizó la última modificación.';


--
-- Name: COLUMN sec_permission.secpercreationuser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secpercreationuser IS 'Usuario que creó el registro.';


--
-- Name: COLUMN sec_permission.secperresource; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secperresource IS 'Indica el recurso expuesto de la api';


--
-- Name: COLUMN sec_permission.secpermethod; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secpermethod IS 'Indica el método del recurso expuesto';


--
-- Name: COLUMN sec_permission.secperoperation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_permission.secperoperation IS 'Indica la opeación que actua sobre el permiso';


--
-- Name: sec_permission_secperidpermission_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_permission_secperidpermission_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_permission_secperidpermission_seq OWNER TO postgres;

--
-- Name: sec_permission_secperidpermission_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_permission_secperidpermission_seq OWNED BY public.sec_permission.secperidpermission;


--
-- Name: sec_person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_person (
    secperid integer NOT NULL,
    secperfirstname character varying(30) NOT NULL,
    secpermiddlename character varying(30),
    secperlastname character varying(30) NOT NULL,
    secpermiddlelastname character varying(30),
    secperdateofbirth timestamp without time zone,
    secperaddress character varying(120),
    secpercreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    secpermodificationdate timestamp without time zone,
    secperusercreation character varying(20) NOT NULL,
    secperusermodification character varying(20)
);


ALTER TABLE public.sec_person OWNER TO postgres;

--
-- Name: TABLE sec_person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_person IS 'Tabla que almacena los datos de una persona';


--
-- Name: COLUMN sec_person.secperid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperid IS 'Identificador único de la persona.';


--
-- Name: COLUMN sec_person.secperfirstname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperfirstname IS 'Primer nombre de la persona.';


--
-- Name: COLUMN sec_person.secpermiddlename; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secpermiddlename IS 'secundo nombre de la persona.';


--
-- Name: COLUMN sec_person.secperlastname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperlastname IS 'Primer apellido de la persona.';


--
-- Name: COLUMN sec_person.secpermiddlelastname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secpermiddlelastname IS 'secundo apellido de la persona.';


--
-- Name: COLUMN sec_person.secperdateofbirth; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperdateofbirth IS 'Fecha de nacimiento de la persona.';


--
-- Name: COLUMN sec_person.secperaddress; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperaddress IS 'Dirección de la persona.';


--
-- Name: COLUMN sec_person.secpercreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secpercreationdate IS 'Fecha y hora de la creación del registro.';


--
-- Name: COLUMN sec_person.secpermodificationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secpermodificationdate IS 'Fecha y hora de la modificación del registro.';


--
-- Name: COLUMN sec_person.secperusercreation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperusercreation IS 'Usuario que creó el registro.';


--
-- Name: COLUMN sec_person.secperusermodification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_person.secperusermodification IS 'Usuario que realizó la última modificación.';


--
-- Name: sec_person_hist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_person_hist (
    hsecperid integer NOT NULL,
    hsecperidperson bigint,
    hsecperfirstname character varying(30),
    hsecpermiddlename character varying(30),
    hsecperlastname character varying(30),
    hsecpermiddlelastname character varying(30),
    hsecperdateofbirth timestamp without time zone,
    hsecperaddress character varying(120),
    hsecpercreationdate timestamp without time zone,
    hsecpermodificationdate timestamp without time zone,
    hsecperusercreation character varying(20),
    hsecperusermodification character varying(20),
    hsecperhistcreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    hsecperoperation character varying(20),
    CONSTRAINT chk_hperperoperation CHECK (((hsecperoperation)::text = ANY ((ARRAY['INSERT'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying])::text[])))
);


ALTER TABLE public.sec_person_hist OWNER TO postgres;

--
-- Name: TABLE sec_person_hist; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_person_hist IS 'Tabla historica transaccional de sec_person.';


--
-- Name: sec_person_hist_hsecperid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_person_hist_hsecperid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_person_hist_hsecperid_seq OWNER TO postgres;

--
-- Name: sec_person_hist_hsecperid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_person_hist_hsecperid_seq OWNED BY public.sec_person_hist.hsecperid;


--
-- Name: sec_person_secperid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_person_secperid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_person_secperid_seq OWNER TO postgres;

--
-- Name: sec_person_secperid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_person_secperid_seq OWNED BY public.sec_person.secperid;


--
-- Name: sec_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_role (
    secrolidrole integer NOT NULL,
    secrolname character varying(100) NOT NULL,
    secroldescription character varying(255),
    secrolcreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    secrolmodificationdate timestamp without time zone,
    secrolcreationuser character varying(50) NOT NULL,
    secrolmodificationuser character varying(50)
);


ALTER TABLE public.sec_role OWNER TO postgres;

--
-- Name: TABLE sec_role; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_role IS 'Tabla que almacena los roles del sistema.';


--
-- Name: COLUMN sec_role.secrolidrole; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secrolidrole IS 'Identificador único del rol.';


--
-- Name: COLUMN sec_role.secrolname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secrolname IS 'Nombre del rol.';


--
-- Name: COLUMN sec_role.secroldescription; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secroldescription IS 'Descripción del rol.';


--
-- Name: COLUMN sec_role.secrolcreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secrolcreationdate IS 'Fecha y hora de creación del registro.';


--
-- Name: COLUMN sec_role.secrolmodificationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secrolmodificationdate IS 'Fecha y hora de la última modificación del registro.';


--
-- Name: COLUMN sec_role.secrolcreationuser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secrolcreationuser IS 'Usuario que creó el registro.';


--
-- Name: COLUMN sec_role.secrolmodificationuser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role.secrolmodificationuser IS 'Usuario que realizó la última modificación.';


--
-- Name: sec_role_permission; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_role_permission (
    secrolperid integer NOT NULL,
    secrolperidrole bigint NOT NULL,
    secrolperidpermission bigint NOT NULL,
    secrolpercreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    secrolpercreationuser character varying(20)
);


ALTER TABLE public.sec_role_permission OWNER TO postgres;

--
-- Name: TABLE sec_role_permission; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_role_permission IS 'Relación entre roles y permisos.';


--
-- Name: COLUMN sec_role_permission.secrolperid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_permission.secrolperid IS 'Clave pimaria.';


--
-- Name: COLUMN sec_role_permission.secrolperidrole; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_permission.secrolperidrole IS 'Identificador del rol.';


--
-- Name: COLUMN sec_role_permission.secrolperidpermission; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_permission.secrolperidpermission IS 'Identificador del permiso.';


--
-- Name: COLUMN sec_role_permission.secrolpercreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_permission.secrolpercreationdate IS 'Fecha de asignación del permiso al rol.';


--
-- Name: COLUMN sec_role_permission.secrolpercreationuser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_permission.secrolpercreationuser IS 'Usuario de creación del registro';


--
-- Name: sec_role_permission_secrolperid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_role_permission_secrolperid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_role_permission_secrolperid_seq OWNER TO postgres;

--
-- Name: sec_role_permission_secrolperid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_role_permission_secrolperid_seq OWNED BY public.sec_role_permission.secrolperid;


--
-- Name: sec_role_secrolidrole_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_role_secrolidrole_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_role_secrolidrole_seq OWNER TO postgres;

--
-- Name: sec_role_secrolidrole_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_role_secrolidrole_seq OWNED BY public.sec_role.secrolidrole;


--
-- Name: sec_role_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_role_user (
    secroluseid integer NOT NULL,
    secroluseiduser bigint NOT NULL,
    secroluseidrole bigint NOT NULL,
    secrolusecreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    secrolusecreationuser character varying(30) NOT NULL
);


ALTER TABLE public.sec_role_user OWNER TO postgres;

--
-- Name: TABLE sec_role_user; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_role_user IS 'Relación entre usuarios y roles.';


--
-- Name: COLUMN sec_role_user.secroluseid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_user.secroluseid IS 'Llave primaria.';


--
-- Name: COLUMN sec_role_user.secroluseiduser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_user.secroluseiduser IS 'Identificador del usuario.';


--
-- Name: COLUMN sec_role_user.secroluseidrole; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_user.secroluseidrole IS 'Identificador del rol.';


--
-- Name: COLUMN sec_role_user.secrolusecreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_user.secrolusecreationdate IS 'Fecha de asignación del rol al usuario.';


--
-- Name: COLUMN sec_role_user.secrolusecreationuser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_role_user.secrolusecreationuser IS 'Usuario relacionado a la creación del registro';


--
-- Name: sec_role_user_secroluseid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_role_user_secroluseid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_role_user_secroluseid_seq OWNER TO postgres;

--
-- Name: sec_role_user_secroluseid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_role_user_secroluseid_seq OWNED BY public.sec_role_user.secroluseid;


--
-- Name: sec_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_user (
    secuseiduser integer NOT NULL,
    secuseusername character varying(20) NOT NULL,
    secusepassword character varying(255) NOT NULL,
    secuseemail character varying(50) NOT NULL,
    secusecreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    secusemodificationdate timestamp without time zone,
    secuseusecreation character varying(20) NOT NULL,
    secuseusemodification character varying(20),
    secusestate character varying(50) NOT NULL,
    secuseidperson bigint,
    CONSTRAINT chk_name_state CHECK (((secusestate)::text = ANY ((ARRAY['ACTIVE'::character varying, 'DISABLED'::character varying, 'INCOMPLETE_PERFIL'::character varying])::text[])))
);


ALTER TABLE public.sec_user OWNER TO postgres;

--
-- Name: TABLE sec_user; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_user IS 'Tabla que almacena los datos de los usuarios.';


--
-- Name: COLUMN sec_user.secuseiduser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secuseiduser IS 'Identificador único del usuario.';


--
-- Name: COLUMN sec_user.secuseusername; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secuseusername IS 'Nombre de usuario único.';


--
-- Name: COLUMN sec_user.secusepassword; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secusepassword IS 'Contraseña encriptada.';


--
-- Name: COLUMN sec_user.secuseemail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secuseemail IS 'Email de registro para nueva cuenta.';


--
-- Name: COLUMN sec_user.secusecreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secusecreationdate IS 'Fecha y hora de creación del registro.';


--
-- Name: COLUMN sec_user.secusemodificationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secusemodificationdate IS 'Fecha y hora de la última modificación del registro.';


--
-- Name: COLUMN sec_user.secuseusecreation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secuseusecreation IS 'Usuario que creó el registro.';


--
-- Name: COLUMN sec_user.secuseusemodification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secuseusemodification IS 'Usuario que realizó la última modificación.';


--
-- Name: COLUMN sec_user.secusestate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secusestate IS 'Estados permitidos';


--
-- Name: COLUMN sec_user.secuseidperson; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user.secuseidperson IS 'PK con per_person.';


--
-- Name: sec_user_hist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sec_user_hist (
    hsecuseidhist integer NOT NULL,
    hsecuseiduser bigint,
    hsecuseusername character varying(20),
    hsecusepassword character varying(255),
    hsecuseemail character varying(50),
    hsecusucreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    hsecusumodificationdate timestamp without time zone,
    hsecuseusercreation character varying(20),
    hsecuseusermodification character varying(20),
    hsecusestate character varying(50),
    hsecuseidperson bigint,
    hsecuseoperation character varying(20),
    hsecusehistcreationdate timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_hsecuseoperation CHECK (((hsecuseoperation)::text = ANY ((ARRAY['INSERT'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying])::text[])))
);


ALTER TABLE public.sec_user_hist OWNER TO postgres;

--
-- Name: TABLE sec_user_hist; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sec_user_hist IS 'Tabla que almacena los datos de los usuarios.';


--
-- Name: COLUMN sec_user_hist.hsecuseidhist; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseidhist IS 'Identificador único del registro historico.';


--
-- Name: COLUMN sec_user_hist.hsecuseiduser; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseiduser IS 'Identificador único del usuario.';


--
-- Name: COLUMN sec_user_hist.hsecuseusername; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseusername IS 'Nombre de usuario único.';


--
-- Name: COLUMN sec_user_hist.hsecusepassword; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecusepassword IS 'Contraseña encriptada.';


--
-- Name: COLUMN sec_user_hist.hsecuseemail; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseemail IS 'Email de registro para nueva cuenta.';


--
-- Name: COLUMN sec_user_hist.hsecusucreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecusucreationdate IS 'Fecha y hora de creación del registro.';


--
-- Name: COLUMN sec_user_hist.hsecusumodificationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecusumodificationdate IS 'Fecha y hora de la última modificación del registro.';


--
-- Name: COLUMN sec_user_hist.hsecuseusercreation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseusercreation IS 'Usuario creación.';


--
-- Name: COLUMN sec_user_hist.hsecuseusermodification; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseusermodification IS 'Usuario modificación.';


--
-- Name: COLUMN sec_user_hist.hsecusestate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecusestate IS 'Id sec_user_state.';


--
-- Name: COLUMN sec_user_hist.hsecuseidperson; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseidperson IS 'Id con per_person.';


--
-- Name: COLUMN sec_user_hist.hsecuseoperation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecuseoperation IS 'Operación';


--
-- Name: COLUMN sec_user_hist.hsecusehistcreationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sec_user_hist.hsecusehistcreationdate IS 'Fecha y hora creación registro historico';


--
-- Name: sec_user_hist_hsecuseidhist_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_user_hist_hsecuseidhist_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_user_hist_hsecuseidhist_seq OWNER TO postgres;

--
-- Name: sec_user_hist_hsecuseidhist_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_user_hist_hsecuseidhist_seq OWNED BY public.sec_user_hist.hsecuseidhist;


--
-- Name: sec_user_secuseiduser_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sec_user_secuseiduser_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sec_user_secuseiduser_seq OWNER TO postgres;

--
-- Name: sec_user_secuseiduser_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sec_user_secuseiduser_seq OWNED BY public.sec_user.secuseiduser;


--
-- Name: log_sec logsecuseid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_sec ALTER COLUMN logsecuseid SET DEFAULT nextval('public.log_sec_logsecuseid_seq'::regclass);


--
-- Name: sec_permission secperidpermission; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_permission ALTER COLUMN secperidpermission SET DEFAULT nextval('public.sec_permission_secperidpermission_seq'::regclass);


--
-- Name: sec_person secperid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_person ALTER COLUMN secperid SET DEFAULT nextval('public.sec_person_secperid_seq'::regclass);


--
-- Name: sec_person_hist hsecperid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_person_hist ALTER COLUMN hsecperid SET DEFAULT nextval('public.sec_person_hist_hsecperid_seq'::regclass);


--
-- Name: sec_role secrolidrole; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role ALTER COLUMN secrolidrole SET DEFAULT nextval('public.sec_role_secrolidrole_seq'::regclass);


--
-- Name: sec_role_permission secrolperid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_permission ALTER COLUMN secrolperid SET DEFAULT nextval('public.sec_role_permission_secrolperid_seq'::regclass);


--
-- Name: sec_role_user secroluseid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_user ALTER COLUMN secroluseid SET DEFAULT nextval('public.sec_role_user_secroluseid_seq'::regclass);


--
-- Name: sec_user secuseiduser; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_user ALTER COLUMN secuseiduser SET DEFAULT nextval('public.sec_user_secuseiduser_seq'::regclass);


--
-- Name: sec_user_hist hsecuseidhist; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_user_hist ALTER COLUMN hsecuseidhist SET DEFAULT nextval('public.sec_user_hist_hsecuseidhist_seq'::regclass);


--
-- Data for Name: log_sec; Type: TABLE DATA; Schema: public; Owner: postgres
--
--
-- Data for Name: sec_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sec_permission (secperidpermission, secpername, secperdescription, secpercreationdate, secpermodificationdate, secpercreationuser, secpermodificationuser, secperresource, secpermethod, secperoperation) FROM stdin;
15	PERMISSION_CREATE	Permite crear los permisos en el sistema	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/permissions	POST	CREATE
17	PERMISSION_DELETE	Permite eliminar un permiso por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/permissions/{id}	DELETE	DELETE
5	PERSON_CREATE	Permite crear el registro de una persona	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/persons	POST	CREATE
8	PERSON_DELETE	Permite eliminar una persona por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/persons/{id}	DELETE	DELETE
21	PERMISSION_UPDATE	Permite actualizar los permisos en el sistema	2025-02-19 21:46:27.158314	2025-02-19 22:27:54.74983	calegria13	calegria13	/api/permissions/{id}	PUT	UPDATE
7	PERSON_UPDATE	Permite actualizar una persona por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/persons/{id}	PUT	UPDATE
3	USER_UPDATE	Permite actualizar un usuario	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/users/{id}	PUT	UPDATE
22	ROLE_ASSIGNMENT_PERMISSION	Permite asignar un permiso a un rol.	2025-02-20 14:18:12.230094	\N	calegria13	\N	/api/roles/{roleId}/permissions/{permissionId}	POST	CREATE
23	ROLE_REMOVE_PERMISSION	Permite desasignar un permiso a un rol.	2025-02-20 15:09:00.985763	\N	calegria13	\N	/api/roles/{roleId}/permissions/{permissionId}	DELETE	DELETE
24	ROLE_LIST_PERMISSIONS	Permite listar todos los permisos asignados a un rol.	2025-02-20 15:10:47.741259	\N	calegria13	\N	/api/roles/{roleId}/permissions	GET	LIST
25	PERMISSION_LIST_ROLES	Obtiene todos los roles que tiene asignado un permiso.	2025-02-20 15:13:26.663291	2025-02-20 18:12:29.032849	calegria13	calegria13	/api/roles/permissions/{permissionId}	GET	LIST
4	USER_DELETE	Permite eliminar un usuario por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/users/{id}	DELETE	DELETE
11	ROLE_CREATE	Permite crear los roles en el sistema	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/roles	CREATE	CREATE
13	ROLE_DELETE	Permite eliminar un rol por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/roles/{id}	DELETE	DELETE
14	ROLE_GET	Permite buscar un rol por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/roles/{id}	GET	GET
9	USER_LIST	Permite listar todos los usuarios registrados en el sistema	2025-01-31 23:40:10.949608	2025-01-31 23:40:10.949608	admin_user	\N	/api/users	GET	LIST
18	PERMISSION_GET	Permite buscar un permiso por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/permissions/{id}	GET	GET
16	PERMISSION_LIST	Permite listar todos los permisos registrados en el sistema	2025-01-31 23:40:10.949608	2025-01-31 23:40:10.949608	admin_user	\N	/api/permissions	GET	LIST
6	PERSON_GET	Permite buscar una persona por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/persons/{id}	GET	GET
12	ROLE_LIST	Permite listar todos los roles registrados en el sistema	2025-01-31 23:40:10.949608	2025-01-31 23:40:10.949608	admin_user	\N	/api/roles	GET	LIST
10	PERSON_LIST	Permite listar todas las personas registradas en el sistema	2025-01-31 23:40:10.949608	2025-01-31 23:40:10.949608	admin_user	\N	/api/persons	GET	LIST
2	USER_GET	Permite buscar un usuario por su id	2025-01-16 21:36:33.366236	2025-01-16 21:36:33.366236	admin_user	\N	/api/users/{id}	GET	GET
\.


--
-- Data for Name: sec_person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sec_person (secperid, secperfirstname, secpermiddlename, secperlastname, secpermiddlelastname, secperdateofbirth, secperaddress, secpercreationdate, secpermodificationdate, secperusercreation, secperusermodification) FROM stdin;
57	ClaudiaY	JimenaY	petroY	RuizX	1987-05-05 12:00:00	Cali Barrio Granada	2025-02-17 16:59:46.299716	2025-02-18 15:26:53.223577	calegria13	calegria13
\.


--
-- Data for Name: sec_person_hist; Type: TABLE DATA; Schema: public; Owner: postgres
--

--
-- Data for Name: sec_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sec_role (secrolidrole, secrolname, secroldescription, secrolcreationdate, secrolmodificationdate, secrolcreationuser, secrolmodificationuser) FROM stdin;
3	APP_ADMIN	Administrador de la aplicación, con permisos para gestionar la funcionalidad de la aplicación.	2025-01-24 19:25:25.469462	\N	SYSTEM_ADMIN	\N
1	SYSTEM_ADMIN	Administrador del sistema, con acceso total a todas las funcionalidades.	2025-01-24 19:25:25.469462	\N	SYSTEM_ADMIN	\N
2	SECURITY_ADMIN	Administrador de seguridad, con permisos de gestión de roles y usuarios.	2025-01-24 19:25:25.469462	\N	SYSTEM_ADMIN	\N
4	AUDITOR	Auditor con acceso solo a la visualización de la información, sin permisos de modificación.	2025-01-24 19:25:25.469462	\N	SYSTEM_ADMIN	\N
5	USER	Usuario estándar con permisos básicos de consulta.	2025-01-24 19:25:25.469462	\N	SYSTEM_ADMIN	\N
6	NEW_USER	Rol temporal asignado a usuarios recién registrados, permitiendo crear su perfil y realizar la configuración inicial.	2025-01-31 02:44:19.557229	\N	SYSTEM_ADMIN	\N
9	USER_LIST	Usuario solo con permisos de listar xxxx	2025-02-18 15:05:27.301753	\N	calegria13	calegria13
\.


--
-- Data for Name: sec_role_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sec_role_permission (secrolperid, secrolperidrole, secrolperidpermission, secrolpercreationdate, secrolpercreationuser) FROM stdin;
20	3	3	2025-02-01 01:07:51.664197	admin_user
12	1	10	2025-02-01 01:07:51.641802	admin_user
8	1	6	2025-02-01 01:07:51.641802	admin_user
16	2	7	2025-02-01 01:07:51.651838	admin_user
4	1	2	2025-02-01 01:07:51.641802	admin_user
19	2	10	2025-02-01 01:07:51.651838	admin_user
17	2	8	2025-02-01 01:07:51.651838	admin_user
31	5	4	2025-02-01 01:07:51.678756	admin_user
24	3	10	2025-02-01 01:07:51.664197	admin_user
25	3	7	2025-02-01 01:07:51.664197	admin_user
26	4	2	2025-02-01 01:07:51.672109	admin_user
29	4	10	2025-02-01 01:07:51.672109	admin_user
30	5	3	2025-02-01 01:07:51.678756	admin_user
28	4	9	2025-02-01 01:07:51.672109	admin_user
27	4	6	2025-02-01 01:07:51.672109	admin_user
2	6	5	2025-02-01 01:07:51.63179	admin_user
6	1	4	2025-02-01 01:07:51.641802	admin_user
18	2	9	2025-02-01 01:07:51.651838	admin_user
10	1	8	2025-02-01 01:07:51.641802	admin_user
34	5	8	2025-02-01 01:07:51.678756	admin_user
33	5	7	2025-02-01 01:07:51.678756	admin_user
21	3	6	2025-02-01 01:07:51.664197	admin_user
22	3	2	2025-02-01 01:07:51.664197	admin_user
15	2	6	2025-02-01 01:07:51.651838	admin_user
13	2	3	2025-02-01 01:07:51.651838	admin_user
11	1	9	2025-02-01 01:07:51.641802	admin_user
23	3	9	2025-02-01 01:07:51.664197	admin_user
5	1	3	2025-02-01 01:07:51.641802	admin_user
9	1	7	2025-02-01 01:07:51.641802	admin_user
14	2	4	2025-02-01 01:07:51.651838	admin_user
36	5	2	2025-02-08 21:20:25.355663	admin_user
37	5	6	2025-02-08 21:20:25.355663	admin_user
38	6	2	2025-02-11 16:59:10.547322	admin_user
41	1	12	2025-02-11 16:59:10.547322	admin_user
42	1	13	2025-02-11 16:59:10.547322	admin_user
43	1	14	2025-02-11 16:59:10.547322	admin_user
44	1	11	2025-02-11 16:59:10.547322	admin_user
45	1	15	2025-02-11 16:59:10.547322	admin_user
46	1	21	2025-02-11 16:59:10.547322	admin_user
47	1	22	2025-02-20 20:16:15.918307	admin_user
48	1	23	2025-02-20 20:16:15.918307	admin_user
49	1	24	2025-02-20 20:16:15.918307	admin_user
52	1	25	2025-02-20 20:16:15.918307	admin_user
54	1	5	2025-02-20 17:54:59.079896	calegria13
\.


--
-- Data for Name: sec_role_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sec_role_user (secroluseid, secroluseiduser, secroluseidrole, secrolusecreationdate, secrolusecreationuser) FROM stdin;
16	35	1	2025-02-17 16:59:46.328557	calegria13
\.


--
-- Data for Name: sec_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sec_user (secuseiduser, secuseusername, secusepassword, secuseemail, secusecreationdate, secusemodificationdate, secuseusecreation, secuseusemodification, secusestate, secuseidperson) FROM stdin;
35	calegria	$2a$10$c/GV0cbcWKvtiVT1J4rGRO5NfXvM5sbaJdrItljfnrQl05Bhe4RLG	caalegria@alineumsoft.com	2025-02-17 16:58:42.889521	2025-02-17 16:59:46.330427	calegria13	calegria13	ACTIVE	57
\.

--
-- Data for Name: sec_user_hist; Type: TABLE DATA; Schema: public; Owner: postgres
--

--
-- Name: log_sec_logsecuseid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.log_sec_logsecuseid_seq', 895, true);


--
-- Name: sec_permission_secperidpermission_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_permission_secperidpermission_seq', 25, true);


--
-- Name: sec_person_hist_hsecperid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_person_hist_hsecperid_seq', 85, true);


--
-- Name: sec_person_secperid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_person_secperid_seq', 58, true);


--
-- Name: sec_role_permission_secrolperid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_role_permission_secrolperid_seq', 54, true);


--
-- Name: sec_role_secrolidrole_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_role_secrolidrole_seq', 9, true);


--
-- Name: sec_role_user_secroluseid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_role_user_secroluseid_seq', 18, true);


--
-- Name: sec_user_hist_hsecuseidhist_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_user_hist_hsecuseidhist_seq', 111, true);


--
-- Name: sec_user_secuseiduser_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sec_user_secuseiduser_seq', 37, true);


--
-- Name: log_sec log_sec_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_sec
    ADD CONSTRAINT log_sec_pkey PRIMARY KEY (logsecuseid);


--
-- Name: sec_permission sec_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_permission
    ADD CONSTRAINT sec_permission_pkey PRIMARY KEY (secperidpermission);


--
-- Name: sec_person_hist sec_person_hist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_person_hist
    ADD CONSTRAINT sec_person_hist_pkey PRIMARY KEY (hsecperid);


--
-- Name: sec_person sec_person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_person
    ADD CONSTRAINT sec_person_pkey PRIMARY KEY (secperid);


--
-- Name: sec_role_permission sec_role_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_permission
    ADD CONSTRAINT sec_role_permission_pkey PRIMARY KEY (secrolperid);


--
-- Name: sec_role sec_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role
    ADD CONSTRAINT sec_role_pkey PRIMARY KEY (secrolidrole);


--
-- Name: sec_role sec_role_secrolname_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role
    ADD CONSTRAINT sec_role_secrolname_key UNIQUE (secrolname);


--
-- Name: sec_role_user sec_role_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_user
    ADD CONSTRAINT sec_role_user_pkey PRIMARY KEY (secroluseid);


--
-- Name: sec_user_hist sec_user_hist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_user_hist
    ADD CONSTRAINT sec_user_hist_pkey PRIMARY KEY (hsecuseidhist);


--
-- Name: sec_user sec_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_user
    ADD CONSTRAINT sec_user_pkey PRIMARY KEY (secuseiduser);


--
-- Name: sec_user sec_user_secuseemail_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_user
    ADD CONSTRAINT sec_user_secuseemail_key UNIQUE (secuseemail);


--
-- Name: sec_user sec_user_secuseusername_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_user
    ADD CONSTRAINT sec_user_secuseusername_key UNIQUE (secuseusername);


--
-- Name: unique_sec_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX unique_sec_person ON public.sec_person USING btree (secperfirstname, secpermiddlename, secperlastname, secpermiddlelastname, secperdateofbirth);


--
-- Name: sec_role_permission fk_secrolepermission_secpermission; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_permission
    ADD CONSTRAINT fk_secrolepermission_secpermission FOREIGN KEY (secrolperidpermission) REFERENCES public.sec_permission(secperidpermission) ON DELETE SET NULL;


--
-- Name: sec_role_permission fk_secrolepermission_secrole; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_permission
    ADD CONSTRAINT fk_secrolepermission_secrole FOREIGN KEY (secrolperidrole) REFERENCES public.sec_role(secrolidrole) ON DELETE SET NULL;


--
-- Name: sec_role_user fk_secroleuser_secrole; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_user
    ADD CONSTRAINT fk_secroleuser_secrole FOREIGN KEY (secroluseidrole) REFERENCES public.sec_role(secrolidrole) ON DELETE SET NULL;


--
-- Name: sec_role_user fk_secroleuser_secuser; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sec_role_user
    ADD CONSTRAINT fk_secroleuser_secuser FOREIGN KEY (secroluseiduser) REFERENCES public.sec_user(secuseiduser) ON DELETE SET NULL;


--
-- PostgreSQL database dump complete
--

