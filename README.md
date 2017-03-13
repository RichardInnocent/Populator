# Populator
A fast and easy method of generating random but representative data.

Values are weighted based on the US population.

This has built-in capabilities to generate the following:
- Weighted male and female forenames (using PersonGenerator)
- Weighted surnames (using PersonGenerator)
- Weighted male and female forms of professions, e.g. host/hostess (using PersonGenerator)
- Weighted email domains (using PersonGenerator)
- Dates (using DateGenerator)
- *Any weighted or unweighted data* (using DataSets)

#Examples

**Generating a random name**
String name = personGenerator.getWeightedFullName();

**Generating a random male forename name**
String name = personGenerator.getWeightedMaleForename();

**Generating a random female profession**
String prof = personGenerator.getWeightedFemaleProfession();

**Creating an unweighted custom data set**
*- computers is an ArrayList<String>*
DataSet<String> computerModels = new DataSet<>(computers, DataType.OTHER);

**Creating a weighted custom data set**
*- computers is an ArrayList<String>
- frequencies is an ArrayList<Double>*
DataSet<String> computerModels = new DataSet<>(computers, frequencies, DataType.OTHER)

**Using the DataSet objects**
*- computers is an ArrayList<String>
- frequencies is an ArrayList<Double>*
DataSet<String> computerModels = new DataSet<>(computers, frequencies, DataType.OTHER)
String computerModel = computerModels.getWeightedValue();

**Using a custom list of surnames within PersonGenerator**
*- computers is an ArrayList<String>
- frequencies is an ArrayList<Double>*
DataSet<String> computerModels = new DataSet<>(computers, frequencies, DataType.OTHER)
String computerModel = computerModels.getWeightedValue();
PersonGenerator personGenerator = PersonGenerator.useCustomDataSet(surnames);