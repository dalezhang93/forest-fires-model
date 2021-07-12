## prerun

install jdk8

install maven

## run

git clone https://github.com/dalezhang93/forest-fires-model.git

如果 github 访问不了,使用以下代理地址

git clone https://github.com.cnpmjs.org/dalezhang93/forest-fires-model.git

-----

git checkout master

mvn package

cd target

nohup java -jar -XX:+UseG1GC -XX:NewRatio=1 forestfires-0.0.1-SNAPSHOT.jar &

