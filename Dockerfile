FROM ubuntu:26.04 AS base
RUN apt-get update && apt-get install -y --no-install-recommends build-essential && apt-get install -y apache2 php libapache2-mod-php
COPY ./WebserverFiles/ /var/www/html
RUN chown -R www-data:www-data /var/www/html
EXPOSE 80
CMD ["apache2ctl", "-D", "FOREGROUND"]
