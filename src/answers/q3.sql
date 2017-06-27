DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `split_colum_into_rows`()
  BEGIN
    DECLARE no_more_rows boolean;
    declare v_id int(11);
    DECLARE v_names varchar(50);
    DECLARE one_name varchar(50);
    DECLARE cur CURSOR FOR
      select id, name  from sometbl;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_rows=true;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
      SHOW ERRORS LIMIT 1;
      ROLLBACK;
    END;
    set no_more_rows = 0;
    START TRANSACTION;
    OPEN cur;
    insert_loop:WHILE(no_more_rows=0) DO
      FETCH cur INTO v_id, v_names;
      IF no_more_rows=TRUE THEN
        LEAVE insert_loop;
      END IF;
      delete from sometbl where id=v_id;
      while length(v_names) > 0 do
        set one_name = SUBSTRING_INDEX(v_names, '|', 1);
        set v_names = substring(v_names, length(one_name) + 2, length(v_names));
        insert into sometbl values(v_id, one_name);
      end while;

    END WHILE insert_loop;
    CLOSE cur;
    COMMIT;

  END$$
DELIMITER ;
