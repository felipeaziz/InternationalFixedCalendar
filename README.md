# International Fixed Calendar (IFC) for Java

This project provides a complete implementation of the **International Fixed Calendar** (also known as the Cotsworth plan or the Eastman plan) for the Java platform, fully integrated with the `java.time` (JSR-310) API.

## What is the International Fixed Calendar?

The International Fixed Calendar is a solar calendar reform proposal that provides a highly regular structure:
- **13 Months**: Each year has 13 months. The extra month, named **Sol**, is inserted between June and July.
- **28 Days per Month**: Every month has exactly 28 days, consisting of exactly 4 weeks.
- **Perpetual Calendar**: Every date falls on the same day of the week every year. For example, the 1st of every month is always a Sunday.
- **Intercalary Days**: 
  - **Year Day**: A year-end holiday (December 29 in IFC) added after the 28th of December to make the total 365 days. It does not have a weekday.
  - **Leap Day**: In leap years, an extra day (June 29 in IFC) is added after the 28th of June.

## Project Structure

The library follows the `java.time.chrono` architecture, making it compatible with standard Java date-time utilities.

- `IFCChronology`: The main entry point for the calendar system.
- `IFCDate`: Represents a date (Year, Month, Day) in the IFC system.
- `IFCMonth`: Enum for the 13 months (January to December, including Sol).
- `IFCDayOfWeek`: Enum for the days of the week (Sunday to Saturday).
- `IFCLocalDateTime` / `IFCZonedDateTime`: Support for date-time and time zones within the IFC system.

## Key Features

- **Integration**: Implements `ChronoLocalDate`, `AbstractChronology`, and other standard interfaces.
- **Conversion**: Seamlessly convert between Gregorian (ISO-8601) and IFC dates.
- **Arithmetic**: Support for `plus`, `minus`, and `until` operations using standard units like `ChronoUnit.MONTHS` and `ChronoUnit.DAYS`.
- **Type Safety**: Strongly typed enums for months and days of the week.

## Usage Example

```java
// Get the current date in IFC
IFCDate today = IFCDate.now();

// Create a specific IFC date (e.g., 1st of Sol, 2024)
IFCDate solFirst = IFCDate.of(2024, IFCMonth.SOL, 1);

// Convert Gregorian to IFC
IFCDate fromIso = IFCDate.from(LocalDate.of(2023, 12, 31)); // Returns Year Day

// Date Arithmetic
IFCDate nextMonth = today.plus(1, ChronoUnit.MONTHS); // Exactly 28 days later
```

## How to Run Tests
The project includes a `Main.java` file that demonstrates various conversion scenarios, leap year handling, and date arithmetic. You can run it directly to see the calendar in action.
