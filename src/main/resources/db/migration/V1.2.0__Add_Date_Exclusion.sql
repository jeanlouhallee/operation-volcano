ALTER TABLE reservations
    ADD CONSTRAINT no_overlapping_dates
        EXCLUDE  USING gist
        ( daterange(check_in, check_out) WITH &&
        );

--based on this post https://stackoverflow.com/questions/37790811/prevent-date-overlap-postgresql