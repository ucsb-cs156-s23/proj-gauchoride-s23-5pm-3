const rideRequestFixtures = {
    oneRideRequest: {
        "id": 1,
        "day": "Monday",
        "course": "Math 4A",
        "startTime": "11:00AM",
        "stopTime": "12:15PM",
        "building": "IV Theatre",
        "room": "1",
        "pickupLocation": "San Rafael Residence Hall Tower",
        "rider": {
            "id": 1,
            "fullName": "Bob John"
        }
    },
    threeRideRequests: [
        {
            "id": 2,
            "day": "Tuesday",
            "course": "ANTH 2",
            "startTime": "8:00AM",
            "stopTime": "9:15PM",
            "building": "Music",
            "room": "LLCH",
            "pickupLocation": "Lot 3",
            "rider": {
                "id": 2,
                "fullName": "Tim Smith"
            }
        },
        {
            "id": 3,
            "day": "Wednesday",
            "course": "CHEM 1B",
            "startTime": "9:30AM",
            "stopTime": "10:45PM",
            "building": "Chemistry",
            "room": "1179",
            "pickupLocation": "Anacapa Residence Hall",
            "rider": {
                "id": 3,
                "fullName": "Peter Parker"
            }
        },
        {
            "id": 4,
            "day": "Thursday",
            "course": "ENGL 23",
            "startTime": "9:30AM",
            "stopTime": "10:45PM",
            "building": "Campbell Hall",
            "room": "1",
            "pickupLocation": "Lot 1",
            "rider": {
                "id": 4,
                "fullName": "Lisa Simpson"
            }
        }
    ]
};

export default rideRequestFixtures;