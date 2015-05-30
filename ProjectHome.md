bSpell is used for checking the spelling of the source code, including java, xml, properties, etc.

# ANT Task #

it includes an ant task
```
      <bspell>
	<configuration spellCheckConfig="${fant.etc.dir}/bspell/spellCheck.config"
		       format="console"
		       dictionary="${fant.etc.dir}/bspell/english.jar"
		       reservedDict="${fant.etc.dir}/bspell/reserved.dict"
		       registry="${fant.etc.dir}/bspell/registry.txt"/>
	<fileset dir="${src.main.java.dir}" includes="**/*.java **/*.properties"/>
	<fileset dir="${src.main.resources.dir}" includes="**/*.xml"/>
      </bspell>
```

bSpell will use the file's extension to detect which parser should be uses, in case of .java, it will load the JavaParser to parse the .java source code

And you can define your own parser in a registry file, here is an example:

```
properties = com.google.bspell.parsers.TxtParser
```

Once done, you can parse the .properties file with the TxtParser

The reservedDict file is used to define the words you want the spell checker to ignore, e.g.

```
general = wiki blog xsd com org google param utils txt lang config dict num esc init dir
java = cxf xsi xjc xjb saaj impl bspell fant
```

The format property is used to dump the checking results, console, html etc.

# Maven Plugin #

## How to install the plugin ##

### 0. If you don't have fant ###
```
> svn co http://fant.googlecode.com/svn/trunk/ fant
> cd fant
> ant
```

### 1. Install bSpell ###

```
> svn co http://bspell.googlecode.com/svn/trunk/ bspell
> cd bspell
> ant install
> ant spell.install
> ant deploy
```

### 2. Build the maven plugin ###

```
> cd bspell/extensions/maven-bspell-plugin
> mvn install
```

### 3. Add the following into the ~/.m2/settings.xml: ###

```
<pluginGroups>
   <pluginGroup>org.apache.maven.plugins</pluginGroup>
</pluginGroups>
```

### 4. Put the following section into your pom.xml: ###

```
    <properties>
      <fant.etc.dir>${env.ANT_HOME}/etc/fant</fant.etc.dir>
    </properties>


    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-bspell-plugin</artifactId>

                <executions>
                    <execution>
                        <phase>validate</phase>
                        <configuration>
			  <spellConfiguration>
			    <spellCheckConfig>${fant.etc.dir}/bspell/spellCheck.config</spellCheckConfig>
			    <format>console</format>
			    <dictionary>${fant.etc.dir}/bspell/english.jar</dictionary>
			    <reservedDict>${fant.etc.dir}/bspell/reserved.dict</reservedDict>
			    <registry>${fant.etc.dir}/bspell/registry.txt</registry>
			  </spellConfiguration>
			  
			  <includes>
			    <include>**/src/**/*.properties</include>
			    <include>**/generated/**/*.properties</include>
			    <include>**/*.java</include>
 			    <include>**/*.xml</include>
			  </includes>
                        </configuration>
                        <goals>
                            <goal>bspell</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
	</plugins>
    </build>
```

**You are all set now!**