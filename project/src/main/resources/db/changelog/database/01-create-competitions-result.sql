CREATE TABLE IF NOT EXISTS competitions_result(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   competition_id uuid,
   competition_detailsssss_id uuid);

ALTER TABLE competitions_result ADD CONSTRAINT FK_COMPETITION FOREIGN KEY (competition_id) REFERENCES competitions(id);
ALTER TABLE competitions_result ADD CONSTRAINT FK_COMPETITION_DETAILS FOREIGN KEY (competition_detailsssss_id) REFERENCES competitions_details(id);