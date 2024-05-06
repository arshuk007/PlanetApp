# PlanetApp
Planet app is an Android application that displays a list of planets. Users can select a planet from the list to view detailed information about the planet. The application is designed to handle offline use cases, ensuring a seamless experience even when the device is not connected to the internet.

## Minimum Viable Product
* Display a paginated list of planets on the main screen.
* Allow users to select a planet to view its details.
* Offline support.
* Minimum SDK version : 29

## Implementation Details
* **Architecture:** Implement MVVM (Model-View-ViewModel) Clean Architecture.
* **Dependency Injection:** Utilize Koin for lightweight dependency injection to provide dependencies across the application modules.
* **Networking:** Use Retrofit for making API calls and Gson for JSON parsing.
* **Testing:** Write unit tests using JUnit and Mockito for business logic and instrumented tests for UI components using Espresso.
* **Offline Support:** Implement caching mechanisms using Room Persistence Library for offline data access.

## Recommendations for Future Improvements
* Enhance the UI with animations and transitions for a more engaging user experience, leveraging Jetpack Compose for modern UI development.
* Integrate database encryption to secure sensitive data stored locally on the device.
* Incorporate error handling and retry mechanisms for network requests to improve robustness.
* Enhance offline support by implementing periodic data syncing when the device is online.
* Expand the test suite by adding more unit test cases to cover additional functionalities and edge cases thoroughly.
  

