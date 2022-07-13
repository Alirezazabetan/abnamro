FROM postgres:9.6
USER postgres
RUN whoami
ADD ./docker/sql/init.sql /docker-entrypoint-init.d/
# RUN chown postgres:postgres /docker-entrypoint-initdb.d/init.sql
ENTRYPOINT ["docker-entrypoint.sh"]
EXPOSE 5432
CMD ["postgres"]