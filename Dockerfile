FROM ses9892/ec2_shopping_server:tagname


WORKDIR /root/git/shopping/shoppingWeb
<<<<<<< HEAD
RUN git pull
RUN mvn package -Dmaven.test.skip=true
=======
RUN ["git","pull"]
RUN ["mvn", "package","-Dmaven.test.skip=trued"]
>>>>>>> 0a23de78919a69aad4e0e555e8cdf595694c1fc5

ENV PORT 8080
EXPOSE $PORT

<<<<<<< HEAD
CMD java -jar target/application-0.0.1-SNAPSHOT.jar
=======
CMD java -jar target/application-0.0.1-SNAPSHOT.jar
>>>>>>> 0a23de78919a69aad4e0e555e8cdf595694c1fc5
