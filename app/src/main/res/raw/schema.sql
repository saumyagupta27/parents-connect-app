CREATE TABLE IF NOT EXISTS users (
    id          INTEGER PRIMARY KEY,
    email       VARCHAR(40),
    password    VARCHAR(20),
    fname       VARCHAR(30),
    lname       VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS sections (

)

CREATE TABLE IF NOT EXISTS students (
    id          INTEGER FOREIGN KEY REFERENCES users(id),
    grade       CHAR(2),
    section     CHAR(2),
);

CREATE TABLE IF NOT EXISTS parents (
    id          INTEGER FOREIGN KEY REFERENCES users(id),
    student_id  INTEGER FOREIGN KEY REFERENCES student(id)
);

CREATE TABLE IF NOT EXISTS teachers (
    id          INTEGER FOREIGN KEY REFERENCES users(id),
    grade       CHAR(2),
    section     CHAR(2)
);


--CREATE TABLE messages (
--    receiver     INTEGER FOREIGN KEY REFERENCES users(id),
--    sender       INTEGER FOREIGN KEY REFERENCES users(id),
--    message      VARCHAR,
--    send_time    DATE,
--    is_outdated  BOOLEAN,  // set this to true when the teacher responds
--    PRIMARY KEY (sender, send_time)
-- );