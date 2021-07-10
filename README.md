# MoonyNavBar

[![](https://jitpack.io/v/doctor-blue/moony_nav_bar_android.svg)](https://jitpack.io/#doctor-blue/moony_nav_bar_android)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

## Flutter version [here](https://github.com/doctor-blue/moony_nav_bar_flutter)
## Donate
<a href="https://www.buymeacoffee.com/doctorblue" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" height="41" width="174"></a>
[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.me/doctorblue00)


## GIF
<img src="https://raw.githubusercontent.com/doctor-blue/moony_nav_bar_android/master/images/demo2.gif" width="272" height="550"/><img src="https://raw.githubusercontent.com/doctor-blue/moony_nav_bar_android/master/images/demo1.gif"  width="272" height="550" />


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
 <com.devcomentry.library.MoonyNavBar
        android:id="@+id/moony_nav_bar"
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
   <com.devcomentry.library.MoonyNavBar
        android:id="@+id/moony_nav_bar"
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


-   Use MoonyNavBar callbacks in your activity
```kotlin
binding.moonyNavBar.onItemSelected = {
    if(it==R.id.your_menu_id){
      //your code
    }
}

binding.moonyNavBar.onItemReselected = {
     if(it==R.id.your_menu_id){
      //your code
    }
}
```

OR

```kotlin
  binding.moonyNavBar.onItemSelectedListener = object : OnItemSelectedListener {
    override fun onItemSelect(id: Int) {
        if (it == R.id.your_menu_id) {
            //your code
        }
    }
}

 binding.moonyNavBar.onItemReselectedListener = object : OnItemReselectedListener {
    override fun onItemReselect(id: Int) {
        if (it == R.id.your_menu_id) {
            //your code
        }
    }

}
```

## If you don't like callback, you can use [Navigation Components](https://developer.android.com/guide/navigation/).
- Setup Navigation Component i.e. Add dependenccy to your project, create a Navigation Graph etc.
- For each Fragment in your Navigation Graph, ensure that the Fragment's `id` is the same as the MenuItems in your Menu i.e res/menu/ folder
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
        android:id="@+id/fourth_fragment"
        android:title="@string/user"
        android:icon="@drawable/ic_outline_account_24"/>

</menu>
```


- Navigation Graph i.e res/navigation/ folder
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/first_fragment"
    tools:ignore="UnusedNavigation">

    <fragment
        android:id="@+id/first_fragment"
        android:name="com.devcomentry.moonynavbar.FirstFragment"
        android:label="fragment_first"
        tools:layout="@layout/fragment_first" >
        <action
            android:id="@+id/action_firstFragment_to_secondFragment"
            app:destination="@id/second_fragment" />
    </fragment>
    <fragment
        android:id="@+id/second_fragment"
        android:name="com.devcomentry.moonynavbar.SecondFragment"
        android:label="fragment_second"
        tools:layout="@layout/fragment_second" >
        <action
            android:id="@+id/action_secondFragment_to_thirdFragment"
            app:destination="@id/third_fragment" />
    </fragment>
    <fragment
        android:id="@+id/third_fragment"
        android:name="com.devcomentry.moonynavbar.ThirdFragment"
        android:label="fragment_third"
        tools:layout="@layout/fragment_third" >
        <action
            android:id="@+id/action_thirdFragment_to_fourthFragment2"
            app:destination="@id/fourth_fragment" />
    </fragment>
    <fragment
        android:id="@+id/fourth_fragment"
        android:name="com.devcomentry.moonynavbar.FourthFragment"
        android:label="fragment_fourth"
        tools:layout="@layout/fragment_fourth" />
</navigation>
```

- In your activity i.e `MainActivity`
```kotlin

    private lateinit var navController: NavController

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.main_fragment)
        binding.moonyNavBar.setupWithNavController(navController)

    }


    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
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
	implementation 'com.github.doctor-blue:moony_nav_bar_android:v2.0.0'
}
```
