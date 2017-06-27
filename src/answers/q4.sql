select _date, count(id) from
  (select
     adddate('2017-06-01', @num:=@num+1)  as _date
   from
     (SELECT n0
      FROM (SELECT 0 AS n0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3) AS z0
        CROSS JOIN (SELECT 0 AS n1 UNION SELECT 4 UNION SELECT 8 UNION SELECT 12) AS z1
        CROSS JOIN (SELECT 0 AS n2 UNION SELECT 16 UNION SELECT 32 UNION SELECT 48) AS z2
        CROSS JOIN (SELECT 0 AS n3 UNION SELECT 64 UNION SELECT 128 UNION SELECT 192) AS z3
        CROSS JOIN (SELECT 0 AS n4 UNION SELECT 256 UNION SELECT 512 UNION SELECT 768) AS z4
        CROSS JOIN (SELECT 0 AS n5 UNION SELECT 1024 UNION SELECT 2048 UNION SELECT 3072) AS z5
     ) seq_num,
     (select @num:=-1) num
  ) date_gen
  left join bugs b on date_gen._date >= b.open_date
                      and (date_gen._date <= b.close_date or b.close_date is null)
where _date <= '2017-06-20'
group by _date;