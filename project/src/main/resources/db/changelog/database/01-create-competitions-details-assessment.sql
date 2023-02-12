CREATE TABLE IF NOT EXISTS competitions_details_assessment(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   competition_details_id UUID,
   profile_id UUID);

ALTER TABLE competitions_details_assessment ADD CONSTRAINT FK_COMPETITIONS_DETAILS_ASSESSMENT_COMPETITIONS_DETAILS FOREIGN KEY (competition_details_id) REFERENCES competitions_details(id);
ALTER TABLE competitions_details_assessment ADD CONSTRAINT FK_COMPETITIONS_DETAILS_ASSESSMENT_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles(id);


