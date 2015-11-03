select * from employees order by emp_no asc limit 150;
mysqldump --routines --events --triggers --order-by-primary  --databases employees  -uroot -psicksick  > employees.sql 

CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name

#DELETE FROM INVOICE WHERE ID > 0;
#SELECT COUNT(*) FROM INVOICE;
#SELECT * FROM INVOICE;

#DELETE FROM INVOICE_NUMBER WHERE ID > 0;
#SELECT COUNT(*) FROM INVOICE_NUMBER;
#SELECT * FROM INVOICE_NUMBER;

#DELETE FROM INVOICE_DATE WHERE ID > 0;
#SELECT COUNT(*) FROM INVOICE_DATE;
#SELECT * FROM INVOICE_DATE;

#SELECT * FROM INVOICE ORDER BY ID DESC;

# SELECT employee by department and country
#select
#        emloyees0_.ID_DEPARTMENT as ID_DEPAR2_1_0_,
#        emloyees0_.ID_EMPLOYEE as ID_EMPLO3_2_0_,
#        employee1_.id as id1_3_1_,
#        employee1_.name as name2_3_1_ 
#    from
#        EDC emloyees0_ 
#    inner join
#        EMPLOYEE employee1_ 
#            on emloyees0_.ID_EMPLOYEE=employee1_.id 
#    where
#        emloyees0_.ID_DEPARTMENT=2 AND  emloyees0_.ID_COUNTRY=2;

