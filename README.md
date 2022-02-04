# E-Healthprojekt OPS5

  

## LogIn-Window

  Nach dem Starten des des Programms öffnet sich das Login-Fenster. In diesem muss der medizinische Mitarbeiter ausgewählt werden, sowie das Passwort eingegeben werden. Dieses lautet für alle Mitarbeiter: **OPS5**.

  
## Datensatz zur Synchronisierung mit der Partnergruppe

Wir haben uns gemeinsam mit der Partnergruppe entschieden einen Datensatz aufzustellen, welche synchronisiert ist. Diesen kann man bei phpMyAdmin über das SQL-Feld aufspielen. Dabei werden zunächst alle Bewegungstabellen zurückgesetzt und dann mit den synchronisierten Daten bespielt.

**WICHTIG**: In den **Editiermodus** gehen zum Kopieren, sonst werden die Anführungsstriche nicht richtig mitgenommen!

 Die SQL-Statements, die kopiert werden müssen, sind die folgenden:

### Zum Leeren der Bewegungstabellen

**WICHTIG**: Hier muss die Checkbox für die Fremschlüsselüberprüfung deaktiviert werden!

SET FOREIGN_KEY_CHECKS=0;
TRUNCATE diagnose;
TRUNCATE fall;
TRUNCATE operation;
TRUNCATE patient;
TRUNCATE prozedur;
TRUNCATE rolle;
SET FOREIGN_KEY_CHECKS=1;

### Zum Befüllen der Datenbank

INSERT INTO `patient` (`pat_id`, `name`, `vorname`, `geburtsdatum`, `blutgruppe`, `geschlecht`, `bearbeiter`, `bearbeiter_zeit`, `ersteller`, `erstell_zeit`, `storniert`, `geburtsort`, `strasse`, `postleitzahl`, `telefonnummer`, `notfall`) VALUES (NULL, 'Naumann', 'Felix', '2001-12-31', '0+', 'm', NULL, NULL, '0101082', current_timestamp(), '0', 'Berlin', 'Kastanienallee 36', '23562', '023585082', '0');

INSERT INTO `patient` (`pat_id`, `name`, `vorname`, `geburtsdatum`, `blutgruppe`, `geschlecht`, `bearbeiter`, `bearbeiter_zeit`, `ersteller`, `erstell_zeit`, `storniert`, `geburtsort`, `strasse`, `postleitzahl`, `telefonnummer`, `notfall`) VALUES (NULL, 'Soliman', 'Ahmad', '1994-01-25', 'A+', 'm', NULL, NULL, '0104006', current_timestamp(), '0', NULL, 'Anschützstraße 7', '23562', '43753456', '0');

INSERT INTO `patient` (`pat_id`, `name`, `vorname`, `geburtsdatum`, `blutgruppe`, `geschlecht`, `bearbeiter`, `bearbeiter_zeit`, `ersteller`, `erstell_zeit`, `storniert`, `geburtsort`, `strasse`, `postleitzahl`, `telefonnummer`,`notfall`) VALUES (NULL, 'Krause', 'Cassandra', '2000-11-29', '0+', 'w', NULL, NULL, '0104054', current_timestamp(), '0', 'Hannover', 'Trendelenburgstraße 44', '23562', '77435190', '0');

INSERT INTO `patient` (`pat_id`, `name`, `vorname`, `geburtsdatum`, `blutgruppe`, `geschlecht`, `bearbeiter`, `bearbeiter_zeit`, `ersteller`, `erstell_zeit`, `storniert`, `geburtsort`, `strasse`, `postleitzahl`, `telefonnummer`,`notfall`) VALUES (NULL, 'Günther', 'Lydia', '2000-07-25', '0+', 'w', NULL, NULL, '0101067', current_timestamp(), '0', 'Burg', 'Maiblumenstraße 4', '23558', '83450043', '0');

INSERT INTO `patient` (`pat_id`, `name`, `vorname`, `geburtsdatum`, `blutgruppe`, `geschlecht`, `bearbeiter`, `bearbeiter_zeit`, `ersteller`, `erstell_zeit`, `storniert`, `geburtsort`, `strasse`, `postleitzahl`, `telefonnummer`,`notfall`) VALUES (NULL, 'Mustermann', 'Max', '1999-06-13', 'B+', 'm', NULL, NULL, '0104007', current_timestamp(), '0', 'Hamburg', 'Musterstraße 42', '12345', '123456789', '0');

INSERT INTO `fall` (`fall_id`, `aufnahmedatum`, `entlassungsdatum`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `pat_id`, `station_st`, `ersteller`, `bearbeiter`, `fall_typ`) VALUES

(NULL, '2022-01-16 12:30:00', NULL, current_timestamp(), NULL, '0', '1', 'HEC', '0104149', NULL, 1),

(NULL, '2022-01-02 08:00:00', '2022-01-10 11:45:00', current_timestamp(), NULL, '0', '1', '9B', '0104032', NULL, 1),

(NULL, '2021-11-21 10:00:00', NULL, current_timestamp(), NULL, '0', '2', '920003', '0104088', NULL, 2),

(NULL, '2021-12-13 17:00:00', NULL, current_timestamp(), NULL, '0', '3', 'ALK', '0104123', NULL, 2),

(NULL, '2022-01-18 11:25:00', NULL, current_timestamp(), NULL, '0', '5', '14', '0101075', NULL, 1)

;

INSERT INTO `operation` (`op_id`, `beginn`, `ende`, `bauchtuecher_prae`, `bauchtuecher_post`, `schnittzeit`, `nahtzeit`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `fall_id`, `op_saal`, `narkose_st`, `op_typ_st`, `ersteller`, `bearbeiter`, `geplant`) VALUES

(NULL, '2022-01-20 10:00:00', '2022-01-20 11:00:00', '1', '1', '2022-01-20 10:05:30', '2022-01-20 10:50:00', current_timestamp(), NULL, '0', '1', '2', '5', '1', '0104126', NULL, '0'),

(NULL, '2022-01-23 08:30:00', '2022-01-23 10:00:00', '0', '0', '2022-01-23 08:32:00', '2022-01-23 09:52:00', current_timestamp(), NULL, '0', '2', '3', '1', '2', '0104126', NULL, '0'),

(NULL, '2022-02-10 13:00:00', '2022-02-10 15:00:00', '3', '3', '2022-02-10 13:00:00', '2022-02-10 14:46:00', current_timestamp(), NULL, '0', '3', '3', '3', '3', '0104126', NULL, '0'),

(NULL, '2022-02-02 08:00:00', '2022-02-02 10:40:00', '1', '1', '2022-02-02 08:03:00', '2022-02-02 10:35:00', current_timestamp(), NULL, '0', '4', '4', '2', '1', '0104126', NULL, '0'),

(NULL, '2022-01-28 09:00:00', '2022-01-28 11:00:00', '1', '1', '2022-01-28 09:01:00', '2022-01-28 10:50:00', current_timestamp(), NULL, '0', '5', '1', '1', '3', '0104126', NULL, '0');

INSERT INTO `rolle` (`op_id`, `bearbeiter`, `rolle_st`, `bearbeiter_zeit`, `erstell_zeit`, `ersteller`, `med_personal_pers_ID`, `storniert`) VALUES

('1', NULL, '3', NULL, current_timestamp(), '0101096', '0104088', '0'),

('2', NULL, '3', NULL, current_timestamp(), '0104126', '0104025', '0'),

('3', NULL, '3', NULL, current_timestamp(), '0101095', '0104085', '0'),

('4', NULL, '3', NULL, current_timestamp(), '00191184', '0104099', '0'),

('5', NULL, '3', NULL, current_timestamp(), '0101124', '0104135', '0'),

('1', NULL, '4', NULL, current_timestamp(), '0101096', '0104135', '0'),

('2', NULL, '4', NULL, current_timestamp(), '0104126', '0104165', '0'),

('5', NULL, '4', NULL, current_timestamp(), '0101124', '0104162', '0')

;

INSERT INTO `diagnose` (`diagnose_id`, `klartext_diagnose`, `datum`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `op_id`, `diagnosetyp`, `icd10_code`, `ersteller`, `bearbeiter`) VALUES (NULL, 'Mangel an Gerinnungsfaktor VIII.\r\nFaktorenanalyse zur Bestätigung ausstehend.', current_timestamp(), current_timestamp(), NULL, '0', 1, '1', 'D66', '0104032', NULL);

INSERT INTO `diagnose` (`diagnose_id`, `klartext_diagnose`, `datum`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `op_id`, `diagnosetyp`, `icd10_code`, `ersteller`, `bearbeiter`) VALUES (NULL, 'Tuberkulose mittels Kultur aus dem Labor festgestellt.', current_timestamp(), current_timestamp(), NULL, '0', '2', '1', 'A15.1', '0101067', NULL);

INSERT INTO `diagnose` (`diagnose_id`, `klartext_diagnose`, `datum`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `op_id`, `diagnosetyp`, `icd10_code`, `ersteller`, `bearbeiter`) VALUES (NULL, 'Nach Sturz äußere Haut der Nase verletzt. GGf. nähen.', '2022-02-24 13:09:44', current_timestamp(), NULL, '0', '3', '2', 'S01.21', '0104054', NULL);

INSERT INTO `diagnose` (`diagnose_id`, `klartext_diagnose`, `datum`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `op_id`, `diagnosetyp`, `icd10_code`, `ersteller`, `bearbeiter`) VALUES (NULL, 'Buntstift ins rechte Auge gesteckt', current_timestamp(), current_timestamp(), NULL, '0', '4', '1', 'T15.0', '0101067', NULL);

INSERT INTO `diagnose` (`diagnose_id`, `klartext_diagnose`, `datum`, `erstell_zeit`, `bearbeiter_zeit`, `storniert`, `op_id`, `diagnosetyp`, `icd10_code`, `ersteller`, `bearbeiter`) VALUES (NULL, 'Alkoholische Fettleber sorgt für Schmerzen, Entzugstherapie empfehlen.', current_timestamp(), current_timestamp(), NULL, '0', '5', '1', 'K70.0', '0101040', NULL);

INSERT INTO `prozedur` (`proz_id`, `anmerkung`, `storniert`, `erstell_zeit`, `bearbeiter_zeit`, `op_id`, `ops_code`, `bearbeiter`, `ersteller`) VALUES (NULL, 'Nachuntersuchung nach abgeschlossener Leber OP.',  '0', current_timestamp(), NULL, '5', '3-055', NULL, '0101067');

INSERT INTO `prozedur` (`proz_id`, `anmerkung`, `storniert`, `erstell_zeit`, `bearbeiter_zeit`, `op_id`, `ops_code`, `bearbeiter`, `ersteller`) VALUES (NULL, 'Biopsie der Chorionzotten wegen Verdacht von Mangel an Gerinnungsfaktor VIII.', '0', current_timestamp(), NULL, '5', '1-473.0', NULL, '0101075');
