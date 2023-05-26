import React from 'react';
import RideRequestForm from "main/components/RideRequests/RideRequestForm"
import rideRequestFixtures from 'fixtures/rideRequestFixtures';

export default {
    title: 'components/RideRequests/RideRequestForm',
    component: RideRequestForm
};

const Template = (args) => {
    return (
        <RideRequestForm {...args} />
    )
};

export const Default = Template.bind({});

Default.args = {
    submitText: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};

export const Show = Template.bind({});

Show.args = {
    initialRideRequest: rideRequestFixtures.oneRideRequest,
    submitText: "",
    submitAction: () => { }
};