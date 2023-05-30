import { render, screen, act, waitFor, fireEvent } from "@testing-library/react";
import ParkEditPage from "main/pages/Rides/RideEditPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import mockConsole from "jest-mock-console";

const mockNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useParams: () => ({
        id: 3
    }),
    useNavigate: () => mockNavigate
}));

const mockUpdate = jest.fn();
jest.mock('main/utils/rideUtils', () => {
    return {
        __esModule: true,
        rideUtils: {
            update: (ride) => {return mockUpdate();},
            getById: (_id) => {
                return {
                    ride: {
                        id: 2,
                        givenName: "Jason",
                        familyName: "Vu",
                        email: "jasonvu@ucsb.edu",
                        admin: "true",
                        driver: "false"
                    }
                }
            }
        }
    }
});



describe("RideEditPage tests", () => {

    const queryClient = new QueryClient();

    test("renders without crashing", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ParkEditPage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("loads the correct fields", async () => {

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ParkEditPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // Might have to edit the display value for true/false (?)


        expect(screen.getByTestId("RideForm-name")).toBeInTheDocument();
        expect(screen.getByDisplayValue('Jason')).toBeInTheDocument();
        expect(screen.getByDisplayValue('Vu')).toBeInTheDocument();
        expect(screen.getByDisplayValue('jasonvu@ucsb.edu')).toBeInTheDocument();
        expect(screen.getByDisplayValue('true')).toBeInTheDocument();
        expect(screen.getByDisplayValue('false')).toBeInTheDocument();

    });

    test("redirects to /Rides on submit", async () => {

        const restoreConsole = mockConsole();

        mockUpdate.mockReturnValue({
            "ride": {
                id: 1,
                givenName: "Phill",
                familyName: "Conrad",
                email: "phtcon@ucsb.edu",
                admin: "true",
                driver: "false"
            }
        });

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ParkEditPage />
                </MemoryRouter>
            </QueryClientProvider>
        )


        // TODO: EDIT THESE VALUES
        const givenNameInput = screen.getByLabelText("First Name");
        expect(givenNameInput).toBeInTheDocument();

        const familyNameInput = screen.getByLabelText("Last Name");
        expect(familyNameInput).toBeInTheDocument();
        
        const emailInput = screen.getByLabelText("Email");
        expect(emailInput).toBeInTheDocument();

        const updateButton = screen.getByText("Update");
        expect(updateButton).toBeInTheDocument();
        

        // TODO: FIX VALUES
        await act(async () => {
            fireEvent.change(givenNameInput, { target: { value: 'Dog Park' } })
            fireEvent.change(addressInput, { target: { value: '123 Ave' } })
            fireEvent.change(ratingInput, { target: { value: '4' } })
            fireEvent.click(updateButton);
        });

        await waitFor(() => expect(mockUpdate).toHaveBeenCalled());
        await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("/Rides"));

        // assert - check that the console.log was called with the expected message
        expect(console.log).toHaveBeenCalled();
        const message = console.log.mock.calls[0][0];
        // TODO: make new test cases
        const expectedMessage =  `updatedPark: {"park":{"id":3,"name":"Dog Park","address":"123 Ave","rating":"4"}`

        expect(message).toMatch(expectedMessage);
        restoreConsole();

    });

});