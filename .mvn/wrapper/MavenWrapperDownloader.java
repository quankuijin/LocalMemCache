/*
 * Copyright 2007-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Properties;

public class MavenWrapperDownloader {

    private static final String WRAPPER_VERSION = "0.5.6";
    private static final String DEFAULT_MAVEN_VERSION = "3.6.3";
    private static final String MAVEN_USER_HOME = System.getProperty("user.home") + File.separator + ".m2";
    private static final String MAVEN_WRAPPER_ROOT = MAVEN_USER_HOME + File.separator + "wrapper";
    private static final String MAVEN_DOWNLOAD_URL = "https://repo.maven.apache.org/maven2";

    public static void main(String[] args) throws Exception {
        String mavenVersion = getMavenVersion();
        File mavenHome = getMavenHome(mavenVersion);
        if (!mavenHome.exists()) {
            System.out.println("Couldn't find Maven wrapper, please install Maven manually.");
            System.exit(1);
        }
    }

    private static String getMavenVersion() {
        return DEFAULT_MAVEN_VERSION;
    }

    private static File getMavenHome(String mavenVersion) {
        return new File(MAVEN_WRAPPER_ROOT, "apache-maven-" + mavenVersion);
    }
}
