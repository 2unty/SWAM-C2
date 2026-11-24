#!/bin/sh
set -e
if ((EUID != 0 )); then
  echo "Run as root"
  exit 1
fi
install_packages() {
    if command -v apt-get >/dev/null 2>&1; then
        sudo apt-get update
        sudo apt-get install -y "apache2 php libapache2-mod-php"
    elif command -v pacman >/dev/null 2>&1; then
        sudo pacman -Syu --noconfirm "apache2 php libapache2-mod-php"
    else
        echo "Error: No supported package manager found." >&2
        exit 1
    fi
}
# Install required packages
install_packages()

# Move webserver files to correct place
for file in updater.php C2dir; do
    if [ ! -f "/var/www/html/$file" ]; then
        cp -r ./WebserverFiles/$file /var/www/html
        if [ "$file" = "updater.php" ]; then
            chmod 755 /var/www/html/$file
        fi
        chown -R www-data:www-data /var/www/html/$file
    fi
done

# Everything is now finished! Turn on apache2 with systemctl and the backend should work.
