--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: author; Type: TABLE; Schema: public; Owner: rokomari
--

CREATE TABLE author (
    id integer NOT NULL,
    code character(50),
    name character(100) NOT NULL,
    dob date,
    email character(50),
    phone character(16),
    facebook character(1000),
    address character(200),
    created date,
    updated date
);


ALTER TABLE author OWNER TO rokomari;

--
-- Name: binder; Type: TABLE; Schema: public; Owner: rokomari
--

CREATE TABLE binder (
    id integer NOT NULL,
    code character(50),
    name character(100) NOT NULL,
    email character(50),
    phone character(16) NOT NULL,
    address character(200),
    created date,
    updated date
);


ALTER TABLE binder OWNER TO rokomari;

--
-- Name: binder_order; Type: TABLE; Schema: public; Owner: rokomari
--

CREATE TABLE binder_order (
    id integer NOT NULL,
    binder_id integer NOT NULL,
    book_quantity integer NOT NULL,
    created date,
    updated date,
    book_received integer NOT NULL
);


ALTER TABLE binder_order OWNER TO rokomari;

--
-- Name: binder_order_product; Type: TABLE; Schema: public; Owner: rokomari
--

CREATE TABLE binder_order_product (
    binder_order_id integer NOT NULL,
    book_id integer NOT NULL,
    book_quantity integer NOT NULL,
    created date,
    updated date
);


ALTER TABLE binder_order_product OWNER TO rokomari;

--
-- Name: binder_order_receive; Type: TABLE; Schema: public; Owner: rokomari
--

CREATE TABLE binder_order_receive (
    binder_order_id integer NOT NULL,
    book_id integer NOT NULL,
    receive_quantity integer NOT NULL,
    created date,
    updated date
);


ALTER TABLE binder_order_receive OWNER TO rokomari;

--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE book (
    id integer NOT NULL,
    author_id integer NOT NULL,
    category_id integer NOT NULL,
    name character(200) NOT NULL,
    code character(50),
    isbn character(13) NOT NULL,
    forma integer,
    list_price double precision NOT NULL,
    unitDiscount double precision NOT NULL,
    sell_price double precision NOT NULL,
    author_royalty double precision,
    inventory_quantity integer NOT NULL,
    created date,
    updated date
);


ALTER TABLE book OWNER TO postgres;

--
-- Name: category; Type: TABLE; Schema: public; Owner: rokomari
--

CREATE TABLE category (
    id integer NOT NULL,
    code character varying(50),
    name character(50),
    created date,
    updated date
);


ALTER TABLE category OWNER TO rokomari;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: rokomari
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO rokomari;

--
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: rokomari
--

COPY author (id, code, name, dob, email, phone, facebook, address, created, updated) FROM stdin;
\.


--
-- Data for Name: binder; Type: TABLE DATA; Schema: public; Owner: rokomari
--

COPY binder (id, code, name, email, phone, address, created, updated) FROM stdin;
\.


--
-- Data for Name: binder_order; Type: TABLE DATA; Schema: public; Owner: rokomari
--

COPY binder_order (id, binder_id, book_quantity, created, updated, book_received) FROM stdin;
\.


--
-- Data for Name: binder_order_product; Type: TABLE DATA; Schema: public; Owner: rokomari
--

COPY binder_order_product (binder_order_id, book_id, book_quantity, created, updated) FROM stdin;
\.


--
-- Data for Name: binder_order_receive; Type: TABLE DATA; Schema: public; Owner: rokomari
--

COPY binder_order_receive (binder_order_id, book_id, receive_quantity, created, updated) FROM stdin;
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY book (id, author_id, category_id, name, code, isbn, forma, list_price, unitDiscount, sell_price, author_royalty, inventory_quantity, created, updated) FROM stdin;
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: rokomari
--

COPY category (id, code, name, created, updated) FROM stdin;
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: rokomari
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- Name: author_pkey; Type: CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- Name: binder_order_pkey; Type: CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder_order
    ADD CONSTRAINT binder_order_pkey PRIMARY KEY (id);


--
-- Name: binder_pkey; Type: CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder
    ADD CONSTRAINT binder_pkey PRIMARY KEY (id);


--
-- Name: book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: fki_binder_order_product_fkey_binder_order; Type: INDEX; Schema: public; Owner: rokomari
--

CREATE INDEX fki_binder_order_product_fkey_binder_order ON binder_order_product USING btree (binder_order_id);


--
-- Name: fki_binder_order_product_fkey_book; Type: INDEX; Schema: public; Owner: rokomari
--

CREATE INDEX fki_binder_order_product_fkey_book ON binder_order_product USING btree (book_id);


--
-- Name: fki_binder_order_receive_fkey_binder_order; Type: INDEX; Schema: public; Owner: rokomari
--

CREATE INDEX fki_binder_order_receive_fkey_binder_order ON binder_order_receive USING btree (binder_order_id);


--
-- Name: fki_binder_order_receive_fkey_book; Type: INDEX; Schema: public; Owner: rokomari
--

CREATE INDEX fki_binder_order_receive_fkey_book ON binder_order_receive USING btree (book_id);


--
-- Name: fki_book_category; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_book_category ON book USING btree (category_id);


--
-- Name: fki_book_fkey_author; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_book_fkey_author ON book USING btree (author_id);


--
-- Name: fki_order_fkey_binder; Type: INDEX; Schema: public; Owner: rokomari
--

CREATE INDEX fki_order_fkey_binder ON binder_order USING btree (binder_id);


--
-- Name: binder_order_product_fkey_binder_order; Type: FK CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder_order_product
    ADD CONSTRAINT binder_order_product_fkey_binder_order FOREIGN KEY (binder_order_id) REFERENCES binder_order(id);


--
-- Name: binder_order_product_fkey_book; Type: FK CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder_order_product
    ADD CONSTRAINT binder_order_product_fkey_book FOREIGN KEY (book_id) REFERENCES book(id);


--
-- Name: binder_order_receive_fkey_binder_order; Type: FK CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder_order_receive
    ADD CONSTRAINT binder_order_receive_fkey_binder_order FOREIGN KEY (binder_order_id) REFERENCES binder_order(id);


--
-- Name: binder_order_receive_fkey_book; Type: FK CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder_order_receive
    ADD CONSTRAINT binder_order_receive_fkey_book FOREIGN KEY (book_id) REFERENCES book(id);


--
-- Name: book_fkey_author; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY book
    ADD CONSTRAINT book_fkey_author FOREIGN KEY (author_id) REFERENCES author(id);


--
-- Name: book_fkey_category; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY book
    ADD CONSTRAINT book_fkey_category FOREIGN KEY (category_id) REFERENCES category(id);


--
-- Name: order_fkey_binder; Type: FK CONSTRAINT; Schema: public; Owner: rokomari
--

ALTER TABLE ONLY binder_order
    ADD CONSTRAINT order_fkey_binder FOREIGN KEY (binder_id) REFERENCES binder(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

