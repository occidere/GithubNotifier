# GithubNotifier (Deprecated)

[![HitCount](http://hits.dwyl.io/occidere/GithubNotifier.svg)](http://hits.dwyl.io/occidere/GithubNotifier)
<img src="https://img.shields.io/github/languages/top/occidere/GithubNotifier"/>
<img src="https://img.shields.io/github/issues/occidere/GithubNotifier"/>
![Gradle Build](https://github.com/occidere/GithubNotifier/workflows/Gradle%20Build/badge.svg)
[![Java Version](https://img.shields.io/badge/Java-11-red.svg)](https://www.java.com/ko/)
[![GitHub license](https://img.shields.io/github/license/occidere/GithubNotifier.svg)](https://github.com/occidere/GithubNotifier/blob/master/LICENSE)

<br>

## **⚠ This project has been moved to [GithubWatcher](https://github.com/occidere/GithubWatcher)**
**This repository will not be updated more.**
**Project should countinue in [GithubWatcher](https://github.com/occidere/GithubWatcher)**

<br>
<br>
<br>
<br>

Multi-purpose github event notification batch.

Currently `Follower change notification`, `Stargazer / Watcher / Fork change notification` are on service, and all changes notified by [Line](https://line.me/en/) messages.

Using Github API to fetch User's github informations, and Elasticsearch as main database.

<br>

## Features

### 1. Follower change notification
Notify follower change both increase and decrease.

#### Examples

![image](https://user-images.githubusercontent.com/20942871/79457506-481c8880-802b-11ea-95d7-3a1e8cb29a28.png)



<br>

### 2. Repository change notification
Notify Stargazer / Watcher / Fork change both increase and decrease.

#### Examples
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
