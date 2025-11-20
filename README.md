# Open Library Android

An Android application that allows users to search and browse books from the Open Library API. Built with modern Android development practices, showcasing Clean Architecture, Jetpack Compose, and comprehensive test coverage.

## Features

- 🔍 Search books by title, author, or keywords
- 📚 Browse book details with descriptions, authors, and subjects
- ♾️ Infinite scrolling with pagination
- 🎨 Modern Material 3 UI with Jetpack Compose
- 🌐 Real-time data from Open Library API

## Technologies

- **Kotlin** - Modern, concise programming language
- **Jetpack Compose** - Declarative UI toolkit
- **Material 3** - Latest Material Design components
- **StateFlow** - Reactive state management with Kotlin Coroutines
- **ViewModel** - Lifecycle-aware state management
- **Hilt** - Dependency injection framework built on top of Dagger
- **Retrofit** - Type-safe HTTP client
- **RxJava 3** - Reactive programming for API calls
- **OkHttp** - HTTP client with logging interceptor
- **Moshi** - JSON serialization/deserialization
- **Jetpack Paging 3** - Pagination library with RxJava integration
- **Coil** - Image loading library

### Testing
- **JUnit** - Unit testing framework
- **Mockito** - Mocking framework
- **Truth** - Assertion library
- **Turbine** - Flow testing library
- **Compose UI Testing** - UI testing for Compose
- **Espresso** - UI testing framework
- **Hilt Testing** - Dependency injection testing support

## Architecture

This project follows **Clean Architecture** principles with **MVVM** & clear separation of concerns across three main layers:

### Presentation Layer
- **Views**: Jetpack Compose screens and components
- **ViewModels**: Manage UI state using `StateFlow` and Coroutines
- **UI Models**: Screen-specific state models (`UiState`)

### Domain Layer
- **Use Cases**: Business logic encapsulation
- **Repository Interfaces**: Data access contracts
- **Domain Models**: Core business entities

### Data Layer
- **Repository Implementations**: Data source coordination
- **API Services**: Network communication
- **Mappers**: Data transformation (Network → Domain)
- **Paging Sources**: Pagination implementation

### Project Structure

```
app/src/main/java/com/fernandohbrasil/openlibraryandroid/
├── presentation/        # UI layer (Compose screens, ViewModels, UI models)
│   ├── books/          # Book details feature
│   ├── search/         # Book search feature
│   └── shared/         # Shared UI components
├── domain/             # Business logic layer
│   ├── books/          # Book domain models and use cases
│   ├── search/         # Search domain models and use cases
│   └── shared/         # Shared domain models
├── data/               # Data layer
│   ├── books/          # Book data sources and repositories
│   ├── search/         # Search data sources and repositories
│   └── shared/         # Shared data utilities
├── di/                 # Hilt dependency injection modules
└── ui/                  # Theme and styling
```

## State Management: Coroutines & StateFlow

This project uses **Kotlin Coroutines** with **StateFlow** for state management in ViewModels, which is the recommended approach when using **Jetpack Compose**.

### Why Coroutines + StateFlow?

1. **Compose Integration**: Jetpack Compose is built with Kotlin Coroutines in mind. The `collectAsState()` extension function seamlessly integrates with `StateFlow`, providing reactive UI updates.

2. **Modern & Type-Safe**: `StateFlow` provides type-safe, observable state containers that work perfectly with Compose's recomposition mechanism.

3. **Lifecycle Awareness**: Combined with `viewModelScope`, Coroutines automatically handle cancellation when the ViewModel is cleared, preventing memory leaks.

4. **Reactive Programming**: StateFlow emits the current state and all subsequent updates, making it ideal for declarative UIs like Compose.

### Alternative: LiveData for XML

If this project needed to support **XML-based layouts** (traditional View system), we would use **LiveData** instead:

- **LiveData** is lifecycle-aware and works seamlessly with XML views through `observe()` methods
- It automatically handles lifecycle subscriptions, preventing crashes when views are destroyed
- However, LiveData is less flexible than StateFlow and doesn't integrate as well with Compose

Since this project is **Compose-only**, StateFlow with Coroutines is the optimal choice for reactive, type-safe state management.

## Test Coverage

The project includes comprehensive test coverage across both unit and UI tests:

### Unit Tests (14 test files)
- **ViewModels**: Testing state management and business logic
- **Use Cases**: Testing domain logic
- **Repositories**: Testing data layer implementations
- **Mappers**: Testing data transformation logic
- **API Services**: Testing network layer

**Testing Tools:**
- JUnit for test structure
- Mockito for mocking dependencies
- Truth for assertions
- Turbine for Flow testing
- Coroutines Test utilities

### UI Tests (2 test files, 6 test cases)
- **BookListScreen Tests**: 
  - Loading state verification
  - Success state with book display
  - Error state with retry functionality
- **BookDetailsBottomSheet Tests**:
  - Loading indicator display
  - Book details display
  - Error state with dismiss button

**Testing Tools:**
- Compose UI Testing framework
- Espresso for UI interactions
- Hilt Testing for dependency injection in tests
- AndroidJUnit4 test runner

### Test Structure

```
app/src/
├── test/              # Unit tests (14 test files)
│   ├── data/          # Data layer tests
│   ├── domain/        # Domain layer tests
│   └── presentation/  # Presentation layer tests
└── androidTest/       # UI/Instrumented tests (2 test files)
    └── presentation/  # UI component tests
```

## License

This project is open source and available under the [MIT License](LICENSE).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

