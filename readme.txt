prvo buildas jar i onda ga prebacis u /home/mato/Desktop/diplomski/test/

a tamo se jos nalazi jedan folder /lib i unutar njega se nalazi potrebni driver za pristup psql.
(postgresql-9.4.1208.jar)


prije prvog pokretanja treba generirat ssh key za stroj na kojem ce se vrtit applikacija,
u ovom slucaju ce to bit raspberry pi...
pokrenit ovo: ssh-keygen -t rsa -b 4096 -C "your_email@example.com"

prati proklete upute tamo i nemoj stavit password na key, kao neki :D

kad to sve bude gotovo, divno krasno pokrenit ovo: ssh-copy-id root@89.107.57.144
upisat fucking password koji se negdje nalazi u mailu :D

prije nego pokrenes app, moras otvorit ssh tunnel, zato sto iz nekog fucking razloga ne radi remote
pristup bazi :D FML.
Ovo pokrenit u terminalu: ssh -L9995:localhost:5432 root@89.107.57.144

usput trebalo bi dodat ovu naredbu da se izvrsava prilikom boot-a

i sad je stvar napokon skoro gotova, pokrenes fucking aplikaciju sa:
sudo java -jar Smartbuilding-1.0-SNAPSHOT.jar

i molis boga da sve radi :D