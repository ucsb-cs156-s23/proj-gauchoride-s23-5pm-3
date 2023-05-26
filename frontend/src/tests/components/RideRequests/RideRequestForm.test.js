import { render, waitFor, fireEvent } from "@testing-library/react";
import RideRequestForm from "main/components/RideRequests/RideRequestForm";
import rideRequestFixtures from "fixtures/rideRequestFixtures";
import { BrowserRouter as Router } from "react-router-dom";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));


describe("RideRequestForm tests", () => {

    test("renders correctly", async () => {

        const { findByText } = render(
            <Router  >
                <RideRequestForm />
            </Router>
        );
        await findByText(/Course/);
        await findByText(/Create/);
    });


    test("renders correctly when passing in a RideRequest", async () => {

        const { getByText, findByTestId } = render(
            <Router  >
                <RideRequestForm initialRideRequest={rideRequestFixtures.oneRideRequest} />
            </Router>
        );
        await findByTestId(/RideRequestForm-userid/);
        expect(getByText(/User ID/)).toBeInTheDocument();
    });

    test("Correct Error messsages on missing input", async () => {

        const { getByTestId, getByText, findByTestId, findByText } = render(
            <Router  >
                <RideRequestForm />
            </Router>
        );
        await findByTestId("RideRequestForm-submit");
        const submitButton = getByTestId("RideRequestForm-submit");

        fireEvent.click(submitButton);

        await findByText(/Day is required/);
        expect(getByText(/Day is required/)).toBeInTheDocument();
        expect(getByText(/Course is required/)).toBeInTheDocument();
        expect(getByText(/Start Time is required/)).toBeInTheDocument();
        expect(getByText(/Stop Time is required/)).toBeInTheDocument();
        expect(getByText(/Building is required/)).toBeInTheDocument();
        expect(getByText(/Room is required/)).toBeInTheDocument();
        expect(getByText(/Pickup Location is required/)).toBeInTheDocument();

    });

    test("No Error messsages on good input", async () => {
        const mockSubmitAction = jest.fn();


        const { getByTestId, queryByText, findByTestId } = render(
            <Router  >
                <RideRequestForm initialRideRequest={rideRequestFixtures.oneRideRequest} submitAction={mockSubmitAction} />
            </Router>
        );
        await findByTestId("RideRequestForm-day");

        const dayField = getByTestId("RideRequestForm-day");
        const courseField = getByTestId("RideRequestForm-course");
        const startTimeField = getByTestId("RideRequestForm-startTime");
        const stopTimeField = getByTestId("RideRequestForm-stopTime");
        const buildingField = getByTestId("RideRequestForm-building");
        const roomField = getByTestId("RideRequestForm-room");
        const pickupField = getByTestId("RideRequestForm-pickup");
        const submitButton = getByTestId("RideRequestForm-submit");

        fireEvent.change(dayField, { target: { value: 'z' } })
        fireEvent.change(courseField, { target: { value: 'z' } })
        fireEvent.change(startTimeField, { target: { value: 'z' } })
        fireEvent.change(stopTimeField, { target: { value: 'z' } })
        fireEvent.change(buildingField, { target: { value: 'z' } })
        fireEvent.change(roomField, { target: { value: 'z' } })
        fireEvent.change(pickupField, { target: { value: 'z' } })
        fireEvent.click(submitButton);

        await waitFor(() => expect(mockSubmitAction).toHaveBeenCalled());

        expect(queryByText(/Address is required/)).not.toBeInTheDocument();

    });


    test("that navigate(-1) is called when Cancel is clicked", async () => {

        const { getByTestId, findByTestId } = render(
            <Router  >
                <RideRequestForm />
            </Router>
        );
        await findByTestId("RideRequestForm-cancel");
        const cancelButton = getByTestId("RideRequestForm-cancel");

        fireEvent.click(cancelButton);

        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));

    });

});