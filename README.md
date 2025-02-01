
# Secure data store for Android 
This library is for storing data securely for sensitive data

# instalation

step 1.
 add it in your root gradle file 

 
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

```

or in new project add this in setting fradle file 

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
}

```

step 2.

add the dipendency

```
dependencies {
	        implementation 'com.github.aliElahi:Secure-Data-Store:1.0.15'
	}

```


## How to use
```
val secureDataStore = SecureDataStoreManager.getSecureDataStore(context = this)
```


now you access the methods can stor and retrive data with `secureDataStore` object

# sample 

 set data : 

```
secureDataStore.setString(key = "test" , value = "data from test")
```

retrive data : 

```
val retrieveData = secureDataStore.getString(key = "test")
```
