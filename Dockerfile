FROM ses9892/ec2_shopping_server:tagname


WORKDIR /root/git/shopping/shoppingWeb
RUN git pull
RUN mvn package -Dmaven.test.skip=true

ENV PORT 8080
EXPOSE $PORT

CMD java -jar target/application-0.0.1-SNAPSHOT.jar
