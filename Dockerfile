FROM ses9892/ec2_shopping_server:tagname


WORKDIR /root/git/shopping/shoppingWeb
RUN ["git","pull"]
RUN ["mvn", "package","-DshipTests"]

ENV PORT 8080
EXPOSE 8080


CMD java -jar target/application-0.0.1-SNAPSHOT.jar
