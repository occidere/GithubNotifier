# GithubNotifier

[![HitCount](http://hits.dwyl.io/occidere/GithubNotifier.svg)](http://hits.dwyl.io/occidere/GithubNotifier)
<img src="https://img.shields.io/github/languages/count/occidere/GithubNotifier"/>
<img src="https://img.shields.io/github/languages/top/occidere/GithubNotifier"/>
<img src="https://img.shields.io/github/issues/occidere/GithubNotifier"/>
![Gradle Build](https://github.com/occidere/GithubNotifier/workflows/Gradle%20Build/badge.svg)
[![Java Version](https://img.shields.io/badge/Java-11-red.svg)](https://www.java.com/ko/)
[![GitHub license](https://img.shields.io/github/license/occidere/GithubNotifier.svg)](https://github.com/occidere/GithubNotifier/blob/master/LICENSE)


Multi-purpose github event notification batch.

Currently `Follower change notification`, `Stargazer / Watcher / Fork change notification` are on service, and all changes notified by [Line](https://line.me/en/) messages.

Using Github API to fetch User's github informations, and Elasticsearch as main database.

<br>

## Examples

### Followers Change
![image](https://user-images.githubusercontent.com/20942871/78133548-90e41700-7459-11ea-9c20-88bf017e75db.png)

<br>

### Stargazers / Watchers / Forks Change
![image](https://user-images.githubusercontent.com/20942871/78133607-b07b3f80-7459-11ea-9ec2-6abfdf3702b0.png)

<br>

## Work Flow
1. Get latest information of user and repository from Github API
2. Get previous information of user and repository from database (Elasticsearch)
3. Comparing both of them to find changes
    - Increase/Decrease of Followers
    - Increate/Decrease of Stargazers
    - Increate/Decrease of Watchers
    - Increate/Decrease of Forks
4. Send notification message on [Line](https://line.me/en/)
5. Save latest information on database


<br>

## Build

### Command Line

#### On-premise Gradle
```bash
gradle build
```

#### Gradle Wrapper
```bash
./gradlew build
```
