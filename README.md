# NoNameBottomBar

[![](https://jitpack.io/v/doctor-blue/NoNameBottomBar.svg)](https://jitpack.io/#doctor-blue/NoNameBottomBar)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

## GIF
<img src="https://raw.githubusercontent.com/doctor-blue/NoNameBottomBar/master/images/demo2.gif" width="272" height="550"/><img src="https://raw.githubusercontent.com/doctor-blue/NoNameBottomBar/master/images/demo1.gif"  width="272" height="550" />


-   Create menu.xml under your res/menu/ folder
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/first_fragment"
        android:title="@string/home"
        android:icon="@drawable/ic_outline_home_24"/>

    <item
        android:id="@+id/second_fragment"
        android:title="@string/notification"
        android:icon="@drawable/ic_outline_notifications_24"
        />

    <item
        android:id="@+id/third_fragment"
        android:title="@string/folder"
        android:icon="@drawable/ic_outline_folder_24"/>
    <item
        android:id="@+id/fourthFragment"
        android:title="@string/user"
        android:icon="@drawable/ic_outline_account_24"/>

</menu>
```

-   Add view into your layout file
## Sample: 
```xml
 <com.doctorblue.noname_library.NoNameBottomBar
        android:id="@+id/no_name_bottombar"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_menu"
        />
```
## Customization
```xml
   <com.doctorblue.noname_library.NoNameBottomBar
        android:id="@+id/no_name_bottombar"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_menu"
        app:indicatorColor=""
        app:iconTintActive=""
        app:iconTint=""
        app:iconSize=""
        app:indicatorType=""
        app:indicatorPosition=""
        app:backgroundColor=""
        app:duration=""
        />
```


-   Use NoNameBottomBar callbacks in your activity
```kotlin
no_name_bottombar.onItemSelected = {
    if(it==R.id.your_menu_id){
      //your code
    }
}

bottomBar.onItemReselected = {
     if(it==R.id.your_menu_id){
      //your code
    }
}
```

OR

```kotlin
  no_name_bottombar.onItemSelectedListener = object : OnItemSelectedListener {
    override fun onItemSelect(id: Int) {
        if (it == R.id.your_menu_id) {
            //your code
        }
    }
}

 no_name_bottombar.onItemReselectedListener = object : OnItemReselectedListener {
    override fun onItemReselect(id: Int) {
        if (it == R.id.your_menu_id) {
            //your code
        }
    }

}
```

## Setup

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
    implementation 'com.github.doctor-blue:NoNameBottomBar:v0.0.2'
}
```
