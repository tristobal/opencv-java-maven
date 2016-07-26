#Proyecto de ejemplo de OpenCV en Java con Maven

Bajar OpenCV
http://opencv.org/downloads.html

Generar el jar
http://docs.opencv.org/3.1.0/df/d65/tutorial_table_of_content_introduction.html

Instalar el jar en repositorio local
```
mvn install:install-file -Dfile=opencv-310.jar -DgroupId=org.opencv -DartifactId=opencv-linux -Dversion=3.1.0 -Dpackaging=jar
```

Comprimir la librería nativa, renombrar a jar e instalar en repositorio local.
```
zip opencvjar-runtime-3.1.0-natives-linux-x86_64.zip libopencv_java310.so
# Para OSX tuve problemas con la librería .so y debió ser renombrada a .dylib
mv opencvjar-runtime-3.1.0-natives-linux-x86_64.zip opencvjar-runtime-3.1.0-natives-linux-x86_64.jar
mvn install:install-file -Dfile=opencvjar-runtime-3.1.0-natives-linux-x86_64.jar -DgroupId=org.opencv -DartifactId=opencv-runtime -Dversion=3.1.0 -Dpackaging=jar -Dclassifier=natives-linux-x86_64

```

Instalar el jar de JavaFX, en el repo local, para que Maven pueda compilar con él
```
mvn install:install-file -Dfile=JAVA_HOME/jre/jfxrt.jar -DgroupId=com.oracle -DartifactId=javafx -Dversion=2.2 -Dpackaging=jar
```

Hecho todo lo anterior, el POM del proyecto debe tener las siguientes dependencias.
```
      <dependency>
          <groupId>com.oracle</groupId>
          <artifactId>javafx</artifactId>
          <version>2.2</version>
      </dependency>

      <dependency>
          <groupId>org.opencv</groupId>
          <artifactId>opencv-linux</artifactId>
          <version>3.1.0</version>
      </dependency>

      <dependency>
          <groupId>org.opencv</groupId>
          <artifactId>opencv-runtime</artifactId>
          <version>3.1.0</version>
          <classifier>natives-linux-x86_64</classifier>
      </dependency>
```

Además, se deben incluir los plugins para que se disponibilice la librería nativa de OpenCV (para la posterior ejecución del proyecto).
```
    <build>
        <plugins>
            <plugin>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.4</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass>cl.tristobal.opencv.App</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>2.1</version>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/lib</outputDirectory>
                            <overWriteReleases>false</overWriteReleases>
                            <overWriteSnapshots>false</overWriteSnapshots>
                            <overWriteIfNewer>true</overWriteIfNewer>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>com.googlecode.mavennatives</groupId>
                <artifactId>maven-nativedependencies-plugin</artifactId>
                <version>0.0.7</version>
                <executions>
                    <execution>
                        <id>unpacknatives</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>copy</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

Compilar el proyecto
```
mvn clean package
```

Ejecutar
```
java -Djava.library.path=target/natives -jar target/NOMBRE_DEL_ARTEFACTO.jar
```