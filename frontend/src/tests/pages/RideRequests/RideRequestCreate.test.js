import { render, waitFor, fireEvent } from "@testing-library/react";
import CreatePage from "main/pages/RideRequests/RideRequestCreatePage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("RideRequestCreatePage tests", () => {

    const axiosMock =new AxiosMockAdapter(axios);

    beforeEach(() => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    });

    test("renders without crashing", () => {
        const queryClient = new QueryClient();
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("when you fill in the form and hit submit, it makes a request to the backend", async () => {

        const queryClient = new QueryClient();
        const RideRequest = {
            id: 17,
            userid: 17,
            day: "Thursday",
            fullName: "Lisa Simpson",
            course: "ENGL 23",
            startTime: "9:30AM",
            stopTime: "10:45PM",
            building: "Campbell Hall",
            room: "1",
            pickup: "Lot 1"
        };

        axiosMock.onPost("/api/RideRequests/post").reply( 202, RideRequest );

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => {
            expect(getByTestId("RideRequestForm-title")).toBeInTheDocument();
        });

        const dayField = getByTestId("RideRequestForm-day");
        const fullNameField = getByTestId("RideRequestForm-fullName");
        const courseField = getByTestId("RideRequestForm-course");
        const startTimeField = getByTestId("RideRequestForm-startTime");
        const stopTimeField = getByTestId("RideRequestForm-stopTime");
        const buildingField = getByTestId("RideRequestForm-building");
        const roomField = getByTestId("RideRequestForm-room");
        const pickupField = getByTestId("RideRequestForm-pickup");
        const submitButton = getByTestId("RideRequestForm-submit");

        fireEvent.change(dayField, { target: { value: 'Thursday' } });
        fireEvent.change(fullNameField, { target: { value: 'Lisa Simpson' } });
        fireEvent.change(courseField, { target: { value: 'ENGL 23' } });
        fireEvent.change(startTimeField, { target: { value: '9:30AM' } });
        fireEvent.change(stopTimeField, { target: { value: '10:45PM' } });
        fireEvent.change(buildingField, { target: { value: 'Campbell Hall' } });
        fireEvent.change(roomField, { target: { value: '1' } });
        fireEvent.change(pickupField, { target: { value: 'Lot 1' } });


        expect(submitButton).toBeInTheDocument();

        fireEvent.click(submitButton);

        await waitFor(() => expect(axiosMock.history.post.length).toBe(1));

        expect(axiosMock.history.post[0].params).toEqual(
            {
                "day": "Thursday",
                "fullName": "Lisa Simpson",
                "course": "ENGL 23",
                "startTime": "9:30AM",
                "stopTime": "10:45PM",
                "building": "Campbell Hall",
                "room": "1",
                "pickup": "Lot 1"
                
        });

        expect(mockToast).toBeCalledWith("New RideRequest Created - id: 17 author: Stephen King");
        expect(mockNavigate).toBeCalledWith({ "to": "/RideRequests/list" });
    });


});


