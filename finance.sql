--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2015-02-05 17:50:02

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 187 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2023 (class 0 OID 0)
-- Dependencies: 187
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 182 (class 1259 OID 118722)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE product (
    id integer NOT NULL,
    name character varying(100),
    project integer
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 118720)
-- Name: Product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "Product_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Product_id_seq" OWNER TO postgres;

--
-- TOC entry 2024 (class 0 OID 0)
-- Dependencies: 181
-- Name: Product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "Product_id_seq" OWNED BY product.id;


--
-- TOC entry 180 (class 1259 OID 118718)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999999999999999
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 118730)
-- Name: month; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE month (
    id integer NOT NULL,
    name character varying(100),
    rusname character varying(100)
);


ALTER TABLE public.month OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 118728)
-- Name: month_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE month_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.month_id_seq OWNER TO postgres;

--
-- TOC entry 2025 (class 0 OID 0)
-- Dependencies: 183
-- Name: month_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE month_id_seq OWNED BY month.id;


--
-- TOC entry 171 (class 1259 OID 118676)
-- Name: position; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "position" (
    id integer NOT NULL,
    title character varying(100)
);


ALTER TABLE public."position" OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 118674)
-- Name: position_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.position_id_seq OWNER TO postgres;

--
-- TOC entry 2026 (class 0 OID 0)
-- Dependencies: 170
-- Name: position_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE position_id_seq OWNED BY "position".id;


--
-- TOC entry 175 (class 1259 OID 118688)
-- Name: project; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE project (
    id integer NOT NULL,
    name character varying(100),
    description text
);


ALTER TABLE public.project OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 118686)
-- Name: project_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE project_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.project_id_seq OWNER TO postgres;

--
-- TOC entry 2027 (class 0 OID 0)
-- Dependencies: 174
-- Name: project_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE project_id_seq OWNED BY project.id;


--
-- TOC entry 176 (class 1259 OID 118695)
-- Name: project_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE project_user (
    student_id integer,
    project_id integer
);


ALTER TABLE public.project_user OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 118738)
-- Name: report; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE report (
    id integer NOT NULL,
    product integer,
    amount integer,
    sold_amount integer,
    price integer,
    cost_price integer,
    create_date timestamp without time zone
);


ALTER TABLE public.report OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 118736)
-- Name: report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.report_id_seq OWNER TO postgres;

--
-- TOC entry 2028 (class 0 OID 0)
-- Dependencies: 185
-- Name: report_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE report_id_seq OWNED BY report.id;


--
-- TOC entry 173 (class 1259 OID 118682)
-- Name: student; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE student (
    id integer NOT NULL,
    name character varying(100),
    password character varying(100),
    email character varying(100),
    logo character varying(100),
    position_id integer,
    current_project integer
);


ALTER TABLE public.student OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 118680)
-- Name: student_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE student_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.student_id_seq OWNER TO postgres;

--
-- TOC entry 2029 (class 0 OID 0)
-- Dependencies: 172
-- Name: student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE student_id_seq OWNED BY student.id;


--
-- TOC entry 178 (class 1259 OID 118700)
-- Name: task; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE task (
    id integer NOT NULL,
    text text,
    done boolean,
    readed boolean,
    project_id integer,
    from_id integer
);


ALTER TABLE public.task OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 118698)
-- Name: task_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.task_id_seq OWNER TO postgres;

--
-- TOC entry 2030 (class 0 OID 0)
-- Dependencies: 177
-- Name: task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE task_id_seq OWNED BY task.id;


--
-- TOC entry 179 (class 1259 OID 118707)
-- Name: task_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE task_user (
    task_id integer,
    student_id integer
);


ALTER TABLE public.task_user OWNER TO postgres;

--
-- TOC entry 2031 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN task_user.task_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN task_user.task_id IS '
';


--
-- TOC entry 1876 (class 2604 OID 118733)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY month ALTER COLUMN id SET DEFAULT nextval('month_id_seq'::regclass);


--
-- TOC entry 1871 (class 2604 OID 118679)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "position" ALTER COLUMN id SET DEFAULT nextval('position_id_seq'::regclass);


--
-- TOC entry 1875 (class 2604 OID 118725)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('"Product_id_seq"'::regclass);


--
-- TOC entry 1873 (class 2604 OID 118691)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY project ALTER COLUMN id SET DEFAULT nextval('project_id_seq'::regclass);


--
-- TOC entry 1877 (class 2604 OID 118741)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report ALTER COLUMN id SET DEFAULT nextval('report_id_seq'::regclass);


--
-- TOC entry 1872 (class 2604 OID 118685)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY student ALTER COLUMN id SET DEFAULT nextval('student_id_seq'::regclass);


--
-- TOC entry 1874 (class 2604 OID 118703)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY task ALTER COLUMN id SET DEFAULT nextval('task_id_seq'::regclass);


--
-- TOC entry 2032 (class 0 OID 0)
-- Dependencies: 181
-- Name: Product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"Product_id_seq"', 1, false);


--
-- TOC entry 2033 (class 0 OID 0)
-- Dependencies: 180
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 70, true);


--
-- TOC entry 2013 (class 0 OID 118730)
-- Dependencies: 184
-- Data for Name: month; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY month (id, name, rusname) FROM stdin;
1	january	январь
2	february	февраль
3	march	март
4	april	апрель
5	may	май
6	june	июнь
7	july	июль
8	august	август
9	september	сентябрь
10	october	октябрь
11	november	ноябрь
12	december	декабрь
\.


--
-- TOC entry 2034 (class 0 OID 0)
-- Dependencies: 183
-- Name: month_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('month_id_seq', 1, false);


--
-- TOC entry 2000 (class 0 OID 118676)
-- Dependencies: 171
-- Data for Name: position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY "position" (id, title) FROM stdin;
1	admin
4	Manager
5	Sales
15	Administartor
\.


--
-- TOC entry 2035 (class 0 OID 0)
-- Dependencies: 170
-- Name: position_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('position_id_seq', 1, false);


--
-- TOC entry 2011 (class 0 OID 118722)
-- Dependencies: 182
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY product (id, name, project) FROM stdin;
9	forsaj	7
11	leviafan	7
8	kingman	7
10	jupiter	7
53	birdman	7
54	justice	7
55	loft	7
\.


--
-- TOC entry 2004 (class 0 OID 118688)
-- Dependencies: 175
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY project (id, name, description) FROM stdin;
1	administration	default project for admin user
7	Movies	movie list for this weekend
\.


--
-- TOC entry 2036 (class 0 OID 0)
-- Dependencies: 174
-- Name: project_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('project_id_seq', 1, false);


--
-- TOC entry 2005 (class 0 OID 118695)
-- Dependencies: 176
-- Data for Name: project_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY project_user (student_id, project_id) FROM stdin;
1	1
7	6
7	16
7	1
\.


--
-- TOC entry 2015 (class 0 OID 118738)
-- Dependencies: 186
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY report (id, product, amount, sold_amount, price, cost_price, create_date) FROM stdin;
33	11	3000	3000	8000	6000	2015-02-01 00:00:00
35	9	3000	2500	10000	7000	2015-01-01 00:00:00
36	9	3000	2900	9500	7000	2015-02-01 00:00:00
38	10	4000	3000	9000	7000	2015-01-01 00:00:00
37	9	3000	3000	9800	7000	2015-03-01 00:00:00
42	9	3000	3000	8000	7000	2015-04-01 00:00:00
44	11	3000	2900	7000	5000	2015-03-01 00:00:00
45	11	3000	3000	7000	5000	2015-04-01 00:00:00
46	11	3000	3000	7500	5000	2015-05-01 00:00:00
47	8	3000	3000	7500	5000	2015-01-01 00:00:00
48	8	3000	3000	8000	5000	2015-02-01 00:00:00
49	8	3000	3000	8500	5000	2015-03-01 00:00:00
50	8	3000	3000	8500	6000	2015-04-01 00:00:00
51	8	3000	2400	8500	5000	2015-05-01 00:00:00
41	10	4000	3800	9200	6000	2015-04-01 00:00:00
40	10	4000	3800	9200	6000	2015-03-01 00:00:00
52	11	3000	2500	8000	4000	2015-06-01 00:00:00
43	9	3000	2600	8500	7000	2015-05-01 00:00:00
56	53	6000	5000	9000	6000	2015-01-01 00:00:00
57	53	6000	5200	9000	6000	2015-02-01 00:00:00
58	53	6000	5500	9000	6000	2015-03-01 00:00:00
59	53	6000	5500	9500	6000	2015-04-01 00:00:00
60	53	6000	5500	9700	6000	2015-05-01 00:00:00
61	53	6000	5500	9700	5500	2015-06-01 00:00:00
62	54	4000	2500	10000	7000	2015-01-01 00:00:00
63	54	4000	2500	9500	7000	2015-02-01 00:00:00
64	54	4000	2500	8000	7000	2015-03-01 00:00:00
65	54	4000	3000	8000	7000	2015-04-01 00:00:00
66	55	3000	3000	8000	5000	2015-01-01 00:00:00
67	55	3000	3000	8000	6000	2015-02-01 00:00:00
68	55	3000	3000	8000	5500	2015-03-01 00:00:00
\.


--
-- TOC entry 2037 (class 0 OID 0)
-- Dependencies: 185
-- Name: report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('report_id_seq', 1, false);


--
-- TOC entry 2002 (class 0 OID 118682)
-- Dependencies: 173
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY student (id, name, password, email, logo, position_id, current_project) FROM stdin;
16	Den	1q2w3e4r	den@gmail.com	C:\\dev\\finance\\upload\\Den-logo.jpg	15	7
6	Scott	1q2w3e4r	scott@gmail.com	C:\\dev\\finance\\upload\\Scott-logo.jpg	4	7
1	admin	123	admin@gmail.com	\N	1	1
\.


--
-- TOC entry 2038 (class 0 OID 0)
-- Dependencies: 172
-- Name: student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('student_id_seq', 1, false);


--
-- TOC entry 2007 (class 0 OID 118700)
-- Dependencies: 178
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY task (id, text, done, readed, project_id, from_id) FROM stdin;
17	qwerty	f	t	7	6
29	add product to sales view	f	t	7	6
30	coke dinner.	f	t	7	1
69	add top 6 product	f	t	7	6
70	add top 6 product char.	f	t	7	6
\.


--
-- TOC entry 2039 (class 0 OID 0)
-- Dependencies: 177
-- Name: task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('task_id_seq', 1, false);


--
-- TOC entry 2008 (class 0 OID 118707)
-- Dependencies: 179
-- Data for Name: task_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY task_user (task_id, student_id) FROM stdin;
17	16
29	16
30	16
30	6
69	16
70	16
\.


--
-- TOC entry 1887 (class 2606 OID 118727)
-- Name: Product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY product
    ADD CONSTRAINT "Product_pkey" PRIMARY KEY (id);


--
-- TOC entry 1889 (class 2606 OID 118735)
-- Name: month_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY month
    ADD CONSTRAINT month_pkey PRIMARY KEY (id);


--
-- TOC entry 1879 (class 2606 OID 118711)
-- Name: position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (id);


--
-- TOC entry 1883 (class 2606 OID 118713)
-- Name: project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 1891 (class 2606 OID 118743)
-- Name: report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_pkey PRIMARY KEY (id);


--
-- TOC entry 1881 (class 2606 OID 118715)
-- Name: student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY student
    ADD CONSTRAINT student_pkey PRIMARY KEY (id);


--
-- TOC entry 1885 (class 2606 OID 118717)
-- Name: task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-02-05 17:50:03

--
-- PostgreSQL database dump complete
--

