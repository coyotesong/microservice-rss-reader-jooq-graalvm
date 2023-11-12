--
-- PostgreSQL database
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

CREATE SCHEMA IF NOT EXISTS rss;
CREATE SCHEMA IF NOT EXISTS rome;

COMMENT ON SCHEMA rss IS 'RSS schema';
COMMENT ON SCHEMA rome IS 'ROME library schema';

SET search_path = public, pg_catalog;