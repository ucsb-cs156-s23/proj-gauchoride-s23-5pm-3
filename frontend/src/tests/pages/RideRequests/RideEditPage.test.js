// c/p team03, need to edit

import { fireEvent, queryByTestId, render, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import BookEditPage from "main/pages/Books/BookEditPage";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

import mockConsole from "jest-mock-console";

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
        useParams: () => ({
            id: 17
        }),
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("BookEditPage tests", () => {

    describe("when the backend doesn't return a book", () => {

        const axiosMock = new AxiosMockAdapter(axios);

        beforeEach(() => {
            axiosMock.reset();
            axiosMock.resetHistory();
            axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
            axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
            axiosMock.onGet("/api/books", { params: { id: 17 } }).timeout();
        });

        const queryClient = new QueryClient();
        test("renders header but table is not present", async () => {

            const restoreConsole = mockConsole();

            const {getByText, queryByTestId, findByText} = render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <BookEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );
            await findByText("Edit Book");
            expect(queryByTestId("BookForm-title")).not.toBeInTheDocument();
            restoreConsole();
        });
    });

    describe("tests where backend is working normally", () => {

        const axiosMock = new AxiosMockAdapter(axios);

        beforeEach(() => {
            axiosMock.reset();
            axiosMock.resetHistory();
            axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
            axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
            axiosMock.onGet("/api/books", { params: { id: 17 } }).reply(200, {
                id: 17,
                title: 'IT',
                author: "Stephen King",
                genre: "Horror"
            });
            axiosMock.onPut('/api/books').reply(200, {
                id: "17",
                title: 'IT2',
                author: "Stephen King2",
                genre: "Horror2"
            });
        });

        const queryClient = new QueryClient();
        test("renders without crashing", () => {
            render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <BookEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );
        });

        test("Is populated with the data provided", async () => {

            const { getByTestId, findByTestId } = render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <BookEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

            await findByTestId("BookForm-title");

            const idField = getByTestId("BookForm-id");
            const titleField = getByTestId("BookForm-title");
            const authorField = getByTestId("BookForm-author");
            const genreField = getByTestId("BookForm-genre");
            const submitButton = getByTestId("BookForm-submit");

            expect(idField).toHaveValue("17");
            expect(titleField).toHaveValue("IT");
            expect(authorField).toHaveValue("Stephen King");
            expect(genreField).toHaveValue("Horror");
        });

        test("Changes when you click Update", async () => {



            const { getByTestId, findByTestId } = render(
                <QueryClientProvider client={queryClient}>
                    <MemoryRouter>
                        <BookEditPage />
                    </MemoryRouter>
                </QueryClientProvider>
            );

            await findByTestId("BookForm-title");

            const idField = getByTestId("BookForm-id");
            const titleField = getByTestId("BookForm-title");
            const authorField = getByTestId("BookForm-author");
            const genreField = getByTestId("BookForm-genre");
            const submitButton = getByTestId("BookForm-submit");

            expect(idField).toHaveValue("17");
            expect(titleField).toHaveValue("IT");
            expect(authorField).toHaveValue("Stephen King");
            expect(genreField).toHaveValue("Horror");

            expect(submitButton).toBeInTheDocument();

            fireEvent.change(titleField, { target: { value: 'IT2' } })
            fireEvent.change(authorField, { target: { value: 'Stephen King2' } })
            fireEvent.change(genreField, { target: { value: "Horror2" } })

            fireEvent.click(submitButton);

            await waitFor(() => expect(mockToast).toBeCalled);
            expect(mockToast).toBeCalledWith("Book Updated - id: 17 title: IT2 author: Stephen King2 genre: Horror2");
            expect(mockNavigate).toBeCalledWith({ "to": "/Books/list" });

            expect(axiosMock.history.put.length).toBe(1); // times called
            expect(axiosMock.history.put[0].params).toEqual({ id: 17 });
            expect(axiosMock.history.put[0].data).toBe(JSON.stringify({
                title: 'IT2',
                author: "Stephen King2",
                genre: "Horror2"
            })); // posted object

        });

       
    });
});


/*

// from team01, going to remove, but keeping some as reference

jest.mock('main/utils/rideUtils', () => {
    return {
        __esModule: true,
        rideUtils: {
            update: (ride) => {return mockUpdate();},
            getById: (_id) => {
                return {
                    ride: {
                        id: 1,
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

    // might rename /Rides here
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
        


        await act(async () => {
            fireEvent.change(givenNameInput, { target: { value: 'Phill' } })
            fireEvent.change(familyNameInput, { target: { value: 'Conrad' } })
            fireEvent.change(emailInput, { target: { value: 'phtcon@ucsb.edu' } })
            fireEvent.click(updateButton);
        });

        // might need to edit /Rides here
        await waitFor(() => expect(mockUpdate).toHaveBeenCalled());
        await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("/Rides"));

        // assert - check that the console.log was called with the expected message
        expect(console.log).toHaveBeenCalled();
        const message = console.log.mock.calls[0][0];
        // TODO: make new test cases
        const expectedMessage =  `updatedRide: {"ride":{"id":1,"First Name":"Phill","Last Name":"Conrad","Email":"phtcon@ucsb.edu"}`

        expect(message).toMatch(expectedMessage);
        restoreConsole();

    });

});
*/