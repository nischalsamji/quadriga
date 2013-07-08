DROP PROCEDURE IF EXISTS sp_showDictionaryNonCollaborators;
DELIMITER $$
CREATE PROCEDURE sp_showDictionaryNonCollaborators
(
	IN indictionaryid		INT,
	OUT errmsg				VARCHAR(200)
)

BEGIN

 -- the error handler for any sql exception
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
      SET errmsg = "SQL exception has occurred";

	IF (errmsg IS NULL)
		THEN SET errmsg = "";
    END IF;

 -- validating the input variables
	 IF(indictionaryid IS NULL)
		 THEN SET errmsg = "dictionaryid cannot be empty";
	 END IF;

	SELECT username FROM tbl_quadriga_user WHERE
	username NOT IN (SELECT collaboratoruser FROM tbl_dictionary_collaborator
	WHERE id = indictionaryid 
	UNION
	SELECT dictionaryowner FROM tbl_dictionary WHERE id = indictionaryid);
END$$
DELIMITER ;

